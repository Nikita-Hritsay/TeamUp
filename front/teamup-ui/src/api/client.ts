import * as authService from '../auth/authService'
import { getAccessToken } from '../auth/tokenStore'

/**
 * Central fetch wrapper that attaches Bearer token to all outbound API requests.
 * Uses token from tokenStore (set by AuthProvider). On 401, attempts one token
 * refresh and retries the request.
 */
export async function fetchWithAuth(
  input: RequestInfo | URL,
  init?: RequestInit,
): Promise<Response> {
  const token = getAccessToken()
  const headers = new Headers(init?.headers)
  if (token) {
    headers.set('Authorization', `Bearer ${token}`)
  }
  let response = await fetch(input, { ...init, headers })

  if (response.status === 401) {
    const newTokens = await authService.refreshToken()
    if (newTokens) {
      const retryHeaders = new Headers(init?.headers)
      retryHeaders.set('Authorization', `Bearer ${newTokens.access_token}`)
      response = await fetch(input, { ...init, headers: retryHeaders })
    }
  }

  return response
}

/**
 * Parse JSON or throw with a consistent error message from API response.
 */
export async function handleResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    let message = 'Request failed'
    try {
      const data = await response.json() as { statusMessage?: string; message?: string; error?: string }
      message =
        (data && (data.statusMessage ?? data.message ?? data.error)) ?? message
    } catch {
      const text = await response.text()
      if (text) message = text
    }
    throw new Error(message)
  }

  if (response.status === 204) {
    return {} as T
  }

  return response.json() as Promise<T>
}
