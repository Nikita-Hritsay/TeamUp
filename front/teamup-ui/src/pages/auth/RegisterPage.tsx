import { useAuth } from '../../context/AuthContext'
import { AuthLayout } from './AuthLayout'
import { useEffect, useState } from 'react'
import { Link, useNavigate } from '../../routerShim'

export function RegisterPage() {
  const { isAuthenticated, isLoading, register, error, clearError } = useAuth()
  const [isRedirecting, setIsRedirecting] = useState(false)
  const navigate = useNavigate()

  useEffect(() => {
    if (isAuthenticated) {
      navigate('/')
      return
    }
  }, [isAuthenticated, navigate])

  useEffect(() => {
    return () => clearError()
  }, [clearError])

  const [errorDisplay, setErrorDisplay] = useState<string | null>(null)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setErrorDisplay(null)
    setIsRedirecting(true)
    try {
      await register()
    } catch (err) {
      setErrorDisplay(err instanceof Error ? err.message : 'Registration failed')
      setIsRedirecting(false)
    }
  }

  const displayError = error ?? errorDisplay
  const busy = isLoading || isRedirecting

  return (
    <AuthLayout title="Create account">
      <form className="auth-form" onSubmit={handleSubmit}>
        {displayError && (
          <div className="auth-error" role="alert">
            {displayError}
          </div>
        )}
        <p className="auth-description">
          Create a new TeamUp account. You will be redirected to the secure
          registration form.
        </p>
        <button type="submit" className="auth-submit" disabled={busy}>
          {isRedirecting ? 'Redirecting…' : 'Create account'}
        </button>
      </form>
      <p className="auth-switch">
        Already have an account? <Link to="/signin">Sign in</Link>
      </p>
    </AuthLayout>
  )
}
