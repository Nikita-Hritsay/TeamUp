/**
 * Module-level token store so the API layer can read the current access token
 * without depending on React. AuthProvider sets this when tokens change.
 */

let currentAccessToken: string | null = null

export function setAccessToken(token: string | null): void {
  currentAccessToken = token
}

export function getAccessToken(): string | null {
  return currentAccessToken
}
