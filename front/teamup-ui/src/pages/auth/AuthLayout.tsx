import type { ReactNode } from 'react'
import { Link } from '../../routerShim'
import './Auth.css'

export function AuthLayout({
  title,
  children,
}: {
  title: string
  children: ReactNode
}) {
  return (
    <div className="auth-page">
      <div className="auth-card">
        <h1 className="auth-title">{title}</h1>
        {children}
        <p className="auth-footer">
          <Link to="/">Back to app</Link>
        </p>
      </div>
    </div>
  )
}
