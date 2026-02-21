import { useAuth } from '../../context/AuthContext'
import * as authService from '../../auth/authService'
import { useEffect, useRef, useState } from 'react'
import { Link, useNavigate } from '../../routerShim'
import './Auth.css'

export function CallbackPage() {
  const { setTokensFromExchange } = useAuth()
  const navigate = useNavigate()
  const [status, setStatus] = useState<'loading' | 'success' | 'error'>('loading')
  const [errorMessage, setErrorMessage] = useState<string | null>(null)
  const exchangeStartedRef = useRef(false)

  useEffect(() => {
    const params = new URLSearchParams(window.location.search)
    const code = params.get('code')
    const state = params.get('state')
    const error = params.get('error')
    const errorDescription = params.get('error_description')

    if (error) {
      setStatus('error')
      setErrorMessage(errorDescription ?? error)
      return
    }

    if (!code || !state) {
      setStatus('error')
      setErrorMessage('Missing code or state in callback.')
      return
    }

    // Prevent double exchange (e.g. React StrictMode or double mount)
    if (exchangeStartedRef.current) return
    exchangeStartedRef.current = true

    let cancelled = false

    authService
      .exchangeCodeForToken(code, state)
      .then((tokens) => {
        // Always apply tokens on success (avoid StrictMode cleanup blocking update)
        setTokensFromExchange(tokens)
        setStatus('success')
        window.history.replaceState({}, '', window.location.pathname)
        navigate('/')
      })
      .catch((err) => {
        if (cancelled) return
        exchangeStartedRef.current = false
        setStatus('error')
        setErrorMessage(err instanceof Error ? err.message : 'Authentication failed')
      })

    return () => {
      cancelled = true
    }
  }, [navigate, setTokensFromExchange])

  if (status === 'success') {
    return (
      <div className="auth-page">
        <div className="auth-card">
          <p className="auth-description">Sign-in successful. Redirecting…</p>
        </div>
      </div>
    )
  }

  if (status === 'error') {
    return (
      <div className="auth-page">
        <div className="auth-card">
          <h1 className="auth-title">Sign-in failed</h1>
          <div className="auth-error" role="alert">
            {errorMessage}
          </div>
          <Link to="/signin" className="auth-link">
            Back to sign in
          </Link>
        </div>
      </div>
    )
  }

  return (
    <div className="auth-page">
      <div className="auth-card">
        <p className="auth-description">Completing sign-in…</p>
      </div>
    </div>
  )
}
