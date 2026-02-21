/**
 * Keycloak / OIDC configuration from environment.
 * All values are read from Vite env (VITE_*) to avoid hardcoding.
 */

const keycloakUrl = import.meta.env.VITE_KEYCLOAK_URL ?? 'http://localhost:7080'
const realm = import.meta.env.VITE_KEYCLOAK_REALM ?? 'TeamUp'
const clientId = import.meta.env.VITE_KEYCLOAK_CLIENT_ID ?? 'teamup-public'
const redirectUri = import.meta.env.VITE_REDIRECT_URI ?? 'http://localhost:5173/callback'

const baseRealmUrl = `${keycloakUrl}/realms/${realm}`

export const authConfig = {
  keycloakUrl,
  realm,
  clientId,
  redirectUri,
  authorizationEndpoint: `${baseRealmUrl}/protocol/openid-connect/auth`,
  tokenEndpoint: `${baseRealmUrl}/protocol/openid-connect/token`,
  logoutEndpoint: `${baseRealmUrl}/protocol/openid-connect/logout`,
  /** Registration: same auth endpoint with kc_action=register */
  registrationEndpoint: `${baseRealmUrl}/protocol/openid-connect/auth`,
} as const

export type AuthConfig = typeof authConfig
