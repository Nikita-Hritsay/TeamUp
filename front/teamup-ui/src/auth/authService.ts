import { authConfig } from './config'
import { generateCodeChallenge, generateCodeVerifier, generateState } from './pkce'
import { setAccessToken } from './tokenStore'

const PKCE_STORAGE_KEY = 'teamup_pkce'
const TOKEN_STORAGE_KEY = 'teamup_tokens'

export type TokenResponse = {
  access_token: string
  refresh_token?: string
  expires_in: number
  token_type: string
  scope?: string
}

type StoredPkce = {
  codeVerifier: string
  state: string
}

function getStoredPkce(): StoredPkce | null {
  try {
    const raw = sessionStorage.getItem(PKCE_STORAGE_KEY)
    if (!raw) return null
    return JSON.parse(raw) as StoredPkce
  } catch {
    return null
  }
}

function clearStoredPkce(): void {
  sessionStorage.removeItem(PKCE_STORAGE_KEY)
}

function storePkce(codeVerifier: string, state: string): void {
  sessionStorage.setItem(
    PKCE_STORAGE_KEY,
    JSON.stringify({ codeVerifier, state }),
  )
}

function getStoredTokens(): TokenResponse | null {
  try {
    const raw = localStorage.getItem(TOKEN_STORAGE_KEY)
    if (!raw) return null
    return JSON.parse(raw) as TokenResponse
  } catch {
    return null
  }
}

function storeTokens(tokens: TokenResponse): void {
  localStorage.setItem(TOKEN_STORAGE_KEY, JSON.stringify(tokens))
  setAccessToken(tokens.access_token)
}

function clearStoredTokens(): void {
  localStorage.removeItem(TOKEN_STORAGE_KEY)
  setAccessToken(null)
}

/**
 * Build authorization URL for Keycloak (login).
 */
export async function buildAuthUrl(): Promise<{ url: string; state: string }> {
  const codeVerifier = generateCodeVerifier()
  const codeChallenge = await generateCodeChallenge(codeVerifier)
  const state = generateState()
  storePkce(codeVerifier, state)

  const params = new URLSearchParams({
    response_type: 'code',
    client_id: authConfig.clientId,
    redirect_uri: authConfig.redirectUri,
    scope: 'openid email',
    state,
    code_challenge: codeChallenge,
    code_challenge_method: 'S256',
  })

  return {
    url: `${authConfig.authorizationEndpoint}?${params.toString()}`,
    state,
  }
}

/**
 * Build registration URL (Keycloak registration form).
 */
export async function buildRegistrationUrl(): Promise<{
  url: string
  state: string
}> {
  const base = await buildAuthUrl()
  const url = `${base.url}&kc_action=register`
  return { url, state: base.state }
}

/**
 * Redirect browser to Keycloak login.
 */
export async function login(): Promise<void> {
  const { url } = await buildAuthUrl()
  window.location.href = url
}

/**
 * Redirect browser to Keycloak registration.
 */
export async function register(): Promise<void> {
  const { url } = await buildRegistrationUrl()
  window.location.href = url
}

/**
 * Validate state and exchange authorization code for tokens.
 */
export async function exchangeCodeForToken(
  code: string,
  returnedState: string,
): Promise<TokenResponse> {
  const stored = getStoredPkce()
  if (!stored) {
    throw new Error('No PKCE data found; session may have expired.')
  }
  if (stored.state !== returnedState) {
    clearStoredPkce()
    throw new Error('Invalid state parameter.')
  }

  const body = new URLSearchParams({
    grant_type: 'authorization_code',
    code,
    redirect_uri: authConfig.redirectUri,
    client_id: authConfig.clientId,
    code_verifier: stored.codeVerifier,
  })

  const response = await fetch(authConfig.tokenEndpoint, {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: body.toString(),
  })

  if (!response.ok) {
    const text = await response.text()
    let message = 'Token exchange failed'
    try {
      const json = JSON.parse(text) as { error_description?: string; error?: string }
      message = json.error_description ?? json.error ?? message
    } catch {
      if (text) message = text
    }
    clearStoredPkce()
    throw new Error(message)
  }

  const tokens = (await response.json()) as TokenResponse
  clearStoredPkce()
  storeTokens(tokens)
  return tokens
}

/**
 * Refresh access token using refresh_token.
 */
export async function refreshToken(): Promise<TokenResponse | null> {
  const current = getStoredTokens()
  if (!current?.refresh_token) return null

  const body = new URLSearchParams({
    grant_type: 'refresh_token',
    client_id: authConfig.clientId,
    refresh_token: current.refresh_token,
  })

  const response = await fetch(authConfig.tokenEndpoint, {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: body.toString(),
  })

  if (!response.ok) {
    clearStoredTokens()
    return null
  }

  const tokens = (await response.json()) as TokenResponse
  storeTokens(tokens)
  if (typeof window !== 'undefined') {
    window.dispatchEvent(new CustomEvent('auth-tokens-updated'))
  }
  return tokens
}

/**
 * Logout: clear tokens and optionally redirect to Keycloak logout.
 */
export function logout(redirectToKeycloak = false): void {
  clearStoredTokens()
  if (redirectToKeycloak) {
    const params = new URLSearchParams({
      post_logout_redirect_uri: window.location.origin,
      client_id: authConfig.clientId,
    })
    window.location.href = `${authConfig.logoutEndpoint}?${params.toString()}`
  }
}

/**
 * Restore token state from storage (e.g. on app load).
 */
export function restoreTokens(): TokenResponse | null {
  const tokens = getStoredTokens()
  if (tokens?.access_token) {
    setAccessToken(tokens.access_token)
    return tokens
  }
  setAccessToken(null)
  return null
}
