import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState,
  type ReactNode,
} from 'react'
import * as authService from '../auth/authService'
import type { TokenResponse } from '../auth/authService'
import { decodeJwtPayload, isTokenExpired } from '../auth/jwt'
import { setAccessToken } from '../auth/tokenStore'

export type AuthUser = {
  sub: string
  preferred_username?: string
  email?: string
  name?: string
  given_name?: string
  family_name?: string
}

export type AuthState = {
  isAuthenticated: boolean
  user: AuthUser | null
  isLoading: boolean
  error: string | null
}

export type AuthContextValue = AuthState & {
  login: () => Promise<void>
  logout: () => void
  register: () => Promise<void>
  getToken: () => string | null
  clearError: () => void
  /** Used by Callback page after code exchange to update context. */
  setTokensFromExchange: (tokens: TokenResponse) => void
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined)

function userFromPayload(payload: ReturnType<typeof decodeJwtPayload>): AuthUser | null {
  if (!payload?.sub) return null
  return {
    sub: payload.sub,
    preferred_username: payload.preferred_username,
    email: payload.email,
    name: payload.name,
    given_name: payload.given_name,
    family_name: payload.family_name,
  }
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [tokens, setTokens] = useState<TokenResponse | null>(() =>
    authService.restoreTokens(),
  )
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const user = useMemo(() => {
    if (!tokens?.access_token) return null
    const payload = decodeJwtPayload(tokens.access_token)
    if (isTokenExpired(payload)) return null
    return userFromPayload(payload)
  }, [tokens?.access_token])

  const isAuthenticated = Boolean(user)

  useEffect(() => {
    if (!tokens?.access_token) {
      setAccessToken(null)
      setIsLoading(false)
      return
    }
    setAccessToken(tokens.access_token)
    setIsLoading(false)
  }, [tokens])

  useEffect(() => {
    const onTokensUpdated = () => {
      const restored = authService.restoreTokens()
      if (restored) setTokens(restored)
    }
    window.addEventListener('auth-tokens-updated', onTokensUpdated)
    return () => window.removeEventListener('auth-tokens-updated', onTokensUpdated)
  }, [])

  const login = useCallback(async () => {
    setError(null)
    await authService.login()
  }, [])

  const logout = useCallback(() => {
    authService.logout(true)
    setTokens(null)
    setError(null)
  }, [])

  const register = useCallback(async () => {
    setError(null)
    await authService.register()
  }, [])

  const getToken = useCallback(() => {
    return tokens?.access_token ?? null
  }, [tokens?.access_token])

  const clearError = useCallback(() => setError(null), [])

  const setTokensFromExchange = useCallback((newTokens: TokenResponse) => {
    setTokens(newTokens)
  }, [])

  const value = useMemo<AuthContextValue>(
    () => ({
      isAuthenticated,
      user,
      isLoading,
      error,
      login,
      logout,
      register,
      getToken,
      clearError,
      setTokensFromExchange,
    }),
    [
      isAuthenticated,
      user,
      isLoading,
      error,
      login,
      logout,
      register,
      getToken,
      clearError,
      setTokensFromExchange,
    ],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth(): AuthContextValue {
  const ctx = useContext(AuthContext)
  if (ctx === undefined) {
    throw new Error('useAuth must be used within AuthProvider')
  }
  return ctx
}

