/**
 * Decode JWT payload without verification (for client-side display only).
 * Backend must validate the token.
 */

export type JwtPayload = {
  sub?: string
  preferred_username?: string
  email?: string
  name?: string
  given_name?: string
  family_name?: string
  exp?: number
  iat?: number
  [key: string]: unknown
}

export function decodeJwtPayload(token: string): JwtPayload | null {
  try {
    const parts = token.split('.')
    if (parts.length !== 3) return null
    const payload = parts[1]
    const base64 = payload.replace(/-/g, '+').replace(/_/g, '/')
    const json = atob(base64)
    return JSON.parse(json) as JwtPayload
  } catch {
    return null
  }
}

export function isTokenExpired(payload: JwtPayload | null): boolean {
  if (!payload?.exp) return true
  const now = Math.floor(Date.now() / 1000)
  return payload.exp <= now
}
