# Keycloak in TeamUp

This document describes how Keycloak is used in the project and the main OAuth 2.0 / OIDC flows: **Authorization Code + PKCE** (for the browser app) and **Client Credentials** (for machine-to-machine). It does not replace the official [Keycloak documentation](https://www.keycloak.org/documentation).

---

## Overview

- **IdP**: Keycloak (e.g. `quay.io/keycloak/keycloak:26.5.3` in Docker).
- **Realm**: `TeamUp` (create it in the admin UI).
- **Usage**:
  - **Browser (TeamUp UI)**: Authorization Code + PKCE; no client secret in the frontend.
  - **Backend / server-to-server**: Client Credentials (optional) for service accounts.
  - **API protection**: The Gateway validates JWTs using Keycloak’s JWK Set; it does not issue tokens.

---

## Running Keycloak

### Docker Compose (recommended)

Keycloak is defined in `docker-compose/default/docker-compose.yml`:

- **Image**: `quay.io/keycloak/keycloak:26.5.3`
- **Port**: `7080:8080`
- **Admin**: `KC_BOOTSTRAP_ADMIN_USERNAME=admin`, `KC_BOOTSTRAP_ADMIN_PASSWORD=admin`
- **Mode**: `start-dev` (dev only; use proper production settings for prod)
- **keycloak-setup**: Optional init container that sets `sslRequired: none` for all realms so HTTP works in dev.

Start with:

```bash
docker compose -f docker-compose/default/docker-compose.yml up -d keycloak
```

Admin console: **http://localhost:7080** (login with admin/admin).

### Standalone (manual)

```bash
docker run -d -p 127.0.0.1:7080:8080 \
  -e KC_BOOTSTRAP_ADMIN_USERNAME=admin \
  -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin \
  quay.io/keycloak/keycloak:26.5.0 start-dev
```

Then create realm `TeamUp` and clients as below.

---

## Realm and clients (minimal setup)

1. Create realm **TeamUp**.
2. Create two clients (or one per flow):

| Purpose              | Client id (example) | Client authentication | Flow / capability |
|----------------------|---------------------|------------------------|-------------------|
| Browser (TeamUp UI)  | `teamup-public`     | **Off** (public)       | Standard Flow (Authorization Code), PKCE |
| Server / automation  | e.g. `teamup-backend` | **On** + Service accounts | Client Credentials |

### Public client (Authorization Code + PKCE)

- **Client type**: OpenID Connect.
- **Client authentication**: **Off** (public client).
- **Standard flow**: **On** (Authorization Code).
- **Direct access grants**: Optional (e.g. Off for pure browser flow).
- **Valid redirect URIs**: e.g. `http://localhost:5173/callback` (match `VITE_REDIRECT_URI` in the UI).
- **Web origins**: e.g. `http://localhost:5173` (CORS).
- **PKCE**: Set to **S256** (recommended) or at least allow PKCE; the TeamUp UI sends `code_challenge` / `code_challenge_method=S256`.

No client secret is used; PKCE protects the token exchange.

### Confidential client (Client Credentials)

- **Client authentication**: **On**.
- **Service accounts roles**: **On** (so you can assign roles and get them in the token if needed).
- **Valid redirect URIs**: Not required for client credentials.
- Use **Client ID** and **Client secret** from the client’s Credentials tab to request tokens:

  ```http
  POST /realms/TeamUp/protocol/openid-connect/token
  Content-Type: application/x-www-form-urlencoded

  grant_type=client_credentials&client_id=...&client_secret=...
  ```

Use this for server-to-server calls (e.g. internal services or scripts), not in the browser.

---

## Flows in detail

### 1. Authorization Code + PKCE (browser / TeamUp UI)

Used when the user logs in from the React app. No client secret is ever in the browser.

1. **Authorize**
   - App generates `code_verifier` (random) and `code_challenge = BASE64URL(SHA256(code_verifier))`, and a `state` (CSRF).
   - User is redirected to:
     - `{keycloak}/realms/TeamUp/protocol/openid-connect/auth?response_type=code&client_id=teamup-public&redirect_uri=...&scope=openid&state=...&code_challenge=...&code_challenge_method=S256`
   - User logs in (and optionally consents) in Keycloak.
   - Keycloak redirects back to `redirect_uri` with `code` and `state`.

2. **Token exchange**
   - App sends to `{keycloak}/realms/TeamUp/protocol/openid-connect/token`:
     - `grant_type=authorization_code`
     - `code=...`
     - `redirect_uri=...` (same as in authorize)
     - `client_id=teamup-public`
     - `code_verifier=...` (Keycloak checks that `BASE64URL(SHA256(code_verifier)) == code_challenge`)
   - Keycloak returns `access_token`, `refresh_token`, `expires_in`, etc.

3. **API calls**
   - App sends `Authorization: Bearer <access_token>` to the Gateway. Gateway validates the JWT using Keycloak’s JWK Set and forwards to Users/Teams.

4. **Refresh**
   - When the access token expires, app calls the same token endpoint with `grant_type=refresh_token` and `refresh_token=...` (and `client_id`). No `code_verifier`; public client.

5. **Logout**
   - App can clear local tokens and optionally redirect to:
     - `{keycloak}/realms/TeamUp/protocol/openid-connect/logout?post_logout_redirect_uri=...&client_id=...`

Implementation in the repo: `front/teamup-ui/src/auth/` (config, PKCE, authService, token store).

---

### 2. Client Credentials (machine-to-machine)

Used when a backend service or script needs to call an API as itself (no user).

1. **Token request**
   - POST to `{keycloak}/realms/TeamUp/protocol/openid-connect/token`:
     - `grant_type=client_credentials`
     - `client_id=...`
     - `client_secret=...`
   - Keycloak returns an `access_token` (no refresh token typically).

2. **API calls**
   - Send `Authorization: Bearer <access_token>` to the Gateway. Same JWT validation as for user tokens.

Use only in trusted backend environments; never expose the client secret in the frontend.

---

## Endpoints (TeamUp realm)

Base URL (HTTP dev): `http://localhost:7080/realms/TeamUp`.

| Endpoint        | Path (relative to realm)                         | Purpose |
|----------------|---------------------------------------------------|--------|
| OIDC discovery | `/.well-known/openid-configuration`              | Get issuer, auth/token/logout/jwks URIs |
| Authorize      | `/protocol/openid-connect/auth`                  | Authorization Code (login) |
| Token          | `/protocol/openid-connect/token`                  | Exchange code, refresh, client credentials |
| Logout         | `/protocol/openid-connect/logout`                | End session redirect |
| JWK Set        | `/protocol/openid-connect/certs`                  | Gateway uses this to validate JWTs |

Full issuer in dev: `http://localhost:7080/realms/TeamUp`. Gateway must be able to reach the JWK Set URL (e.g. `http://keycloak:8080/realms/TeamUp/protocol/openid-connect/certs` from inside Docker).

---

## Gateway integration

- **JWT validation**: Gateway uses `spring-security-oauth2-resource-server` with `jwk-set-uri` pointing to Keycloak’s `/realms/TeamUp/protocol/openid-connect/certs`.
- **No token issuance**: Gateway only validates; all tokens are issued by Keycloak.
- **Protected paths**: `/USERS/**` and `/TEAMS/**` require an authenticated (valid JWT) request. CORS is set for the UI origin (e.g. `http://localhost:5173`).

See **Gateway Server** README for SecurityConfig and route details.

---

## Registration (optional)

Keycloak can show a registration page. The UI can open the same auth URL with an extra parameter: `kc_action=register`. Example: `.../auth?...&kc_action=register`. Implementation: `front/teamup-ui/src/auth/authService.ts` (`buildRegistrationUrl`).

---

## Summary

| Flow                    | Used by        | Client type   | Secret in client? | Main use case        |
|-------------------------|----------------|---------------|-------------------|-----------------------|
| Authorization Code+PKCE | TeamUp UI      | Public        | No                | User login in browser |
| Client Credentials      | Backend/scripts| Confidential  | Yes (server-only) | Service-to-service    |

All access to `/USERS/**` and `/TEAMS/**` goes through the Gateway with a Keycloak-issued JWT; Keycloak is the single place for identity and token issuance.
