# TeamUp UI (Frontend)

## Overview

React-based SPA for the TeamUp microservices system. Uses Vite, TypeScript, and Keycloak (OIDC) with **Authorization Code + PKCE** for browser login. All API calls to the gateway or backend services send a Bearer JWT and support token refresh on 401.

## Tech Stack

- **Runtime**: Node.js
- **Build**: Vite 7.x
- **Framework**: React 19.x
- **Language**: TypeScript 5.9
- **Auth**: Keycloak OIDC (Authorization Code + PKCE), no Keycloak JS adapter

## Security & Auth Flow

- **Flow**: Authorization Code + PKCE (no client secret in the browser).
- **Config**: `VITE_KEYCLOAK_*` and `VITE_REDIRECT_URI` (see `.env.example`).
- **Endpoints used**:
  - Authorization: `{keycloak}/realms/{realm}/protocol/openid-connect/auth`
  - Token: `{keycloak}/realms/{realm}/protocol/openid-connect/token`
  - Logout: `{keycloak}/realms/{realm}/protocol/openid-connect/logout`
- **Token storage**: Access/refresh in `localStorage`; PKCE `code_verifier` and `state` in `sessionStorage` during login.
- **API client**: `src/api/client.ts` — `fetchWithAuth()` adds `Authorization: Bearer <access_token>` and retries once on 401 after refreshing the token.

## Project Structure (relevant)

- `src/auth/` — Keycloak config, PKCE helpers, token exchange, refresh, logout, token store.
- `src/context/AuthContext.tsx` — Auth state and login/logout/register for the app.
- `src/api/` — `client.ts` (fetch + Bearer), `usersApi.ts`, `teamsApi.ts`, `cardsApi.ts`.
- `src/pages/auth/` — Sign-in, Register, callback (code exchange).

## Steps to Run

### Prerequisites

- Node.js (LTS)
- Keycloak running (e.g. `http://localhost:7080`) with realm `TeamUp` and a **public** client (e.g. `teamup-public`) with Standard Flow enabled and valid redirect URI `http://localhost:5173/callback`.

### Install and dev server

```bash
cd front/teamup-ui
npm install
npm run dev
```

App: **http://localhost:5173**

### Using the Gateway

To call APIs via the gateway (e.g. `http://localhost:8072`) instead of direct service URLs, set in `.env`:

- `VITE_USERS_API_URL=http://localhost:8072/USERS/api/v1`
- `VITE_TEAMS_API_URL=http://localhost:8072/TEAMS/api/v1/teams`
- `VITE_CARDS_API_URL=http://localhost:8072/TEAMS/api/v1/cards`

Gateway expects a valid JWT; the UI sends the Keycloak access token as Bearer.

### Build for production

```bash
npm run build
npm run preview   # optional: local preview of build
```

## Environment Variables

| Variable | Description | Default |
|----------|-------------|--------|
| `VITE_KEYCLOAK_URL` | Keycloak base URL | `http://localhost:7080` |
| `VITE_KEYCLOAK_REALM` | Realm name | `TeamUp` |
| `VITE_KEYCLOAK_CLIENT_ID` | Public client id | `teamup-public` |
| `VITE_REDIRECT_URI` | Redirect after login | `http://localhost:5173/callback` |
| `VITE_USERS_API_URL` | Users API base | `http://localhost:8080/api/v1` |
| `VITE_TEAMS_API_URL` | Teams API base | `http://localhost:8081/api/v1/teams` |
| `VITE_CARDS_API_URL` | Cards API base | `http://localhost:8081/api/v1/cards` |

## Docker

This app is not part of the root `docker-compose` by default. To run in Docker you would build a static bundle (`npm run build`) and serve it with nginx or another web server; ensure Keycloak and gateway URLs are correct for the environment (e.g. host names when running in containers).

## OpenAPI / API docs

The UI consumes REST APIs from Users, Teams, and Cards (Teams). OpenAPI/Swagger is provided by the backend services (Users and Teams), not by the frontend. See **Users** and **Teams** module READMEs for doc URLs.
