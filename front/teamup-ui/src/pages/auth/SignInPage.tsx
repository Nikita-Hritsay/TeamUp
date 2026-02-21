import { useAuth } from '../../context/AuthContext'
import { AuthLayout } from './AuthLayout'
import { useEffect, useState } from 'react'
import { Link, useNavigate } from '../../routerShim'

export function SignInPage() {
  const { isAuthenticated, isLoading, login, error, clearError } = useAuth()
  const [isRedirecting, setIsRedirecting] = useState(false)
  const navigate = useNavigate()

  useEffect(() => {
    if (isAuthenticated) {
      navigate('/')
      return
    }
  }, [isAuthenticated, navigate])

  // If Keycloak redirected here with code/state (e.g. redirect_uri is /signin), hand off to callback
  useEffect(() => {
    const params = new URLSearchParams(window.location.search)
    const code = params.get('code')
    const state = params.get('state')
    if (code && state) {
      navigate(`/callback?${window.location.search}`)
    }
  }, [navigate])

  useEffect(() => {
    return () => clearError()
  }, [clearError])

  const [errorDisplay, setErrorDisplay] = useState<string | null>(null)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setErrorDisplay(null)
    setIsRedirecting(true)
    try {
      await login()
    } catch (err) {
      setErrorDisplay(err instanceof Error ? err.message : 'Sign in failed')
      setIsRedirecting(false)
    }
  }

  const displayError = error ?? errorDisplay
  const busy = isLoading || isRedirecting

  return (
    <AuthLayout title="Sign in">
      <form className="auth-form" onSubmit={handleSubmit}>
        {displayError && (
          <div className="auth-error" role="alert">
            {displayError}
          </div>
        )}
        <p className="auth-description">
          Sign in with your TeamUp Keycloak account. You will be redirected to the
          secure login page.
        </p>
        <button type="submit" className="auth-submit" disabled={busy}>
          {isRedirecting ? 'Redirecting…' : 'Sign in'}
        </button>
      </form>
      <p className="auth-switch">
        Don&apos;t have an account? <Link to="/register">Create account</Link>
      </p>
    </AuthLayout>
  )
}
