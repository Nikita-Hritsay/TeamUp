/**
 * PKCE (Proof Key for Code Exchange) helpers for Authorization Code flow.
 * Uses Web Crypto API; no external dependencies.
 */

const CODE_VERIFIER_LENGTH = 64
const STATE_LENGTH = 32

function randomBytes(length: number): Uint8Array {
  const array = new Uint8Array(length)
  crypto.getRandomValues(array)
  return array
}

function base64UrlEncode(buffer: ArrayBufferLike): string {
  const bytes = new Uint8Array(buffer)
  let binary = ''
  for (let i = 0; i < bytes.length; i++) {
    binary += String.fromCharCode(bytes[i])
  }
  return btoa(binary).replace(/\+/g, '-').replace(/\//g, '_').replace(/=+$/, '')
}

/**
 * Generate a cryptographically random code_verifier (43–128 chars, unreserved).
 */
export function generateCodeVerifier(): string {
  const bytes = randomBytes(CODE_VERIFIER_LENGTH)
  return base64UrlEncode(bytes.buffer as ArrayBuffer)
}

/**
 * Generate code_challenge from code_verifier using SHA-256 (S256 method).
 */
export async function generateCodeChallenge(verifier: string): Promise<string> {
  const encoder = new TextEncoder()
  const data = encoder.encode(verifier)
  const digest = await crypto.subtle.digest('SHA-256', data)
  return base64UrlEncode(digest as ArrayBuffer)
}

/**
 * Generate a random state value for CSRF protection.
 */
export function generateState(): string {
  const bytes = randomBytes(STATE_LENGTH)
  return base64UrlEncode(bytes.buffer as ArrayBuffer)
}

export const PKCE = {
  generateCodeVerifier,
  generateCodeChallenge,
  generateState,
} as const
