import { useState, useEffect } from 'react'
import { useAuth } from '../context/AuthContext'

const USER_ID_STORAGE_KEY = 'teamup_user_id'

export function getStoredUserId(): number | null {
  const raw = localStorage.getItem(USER_ID_STORAGE_KEY)
  if (raw == null) return null
  const n = parseInt(raw, 10)
  return Number.isNaN(n) ? null : n
}

export function setStoredUserId(id: number | null): void {
  if (id == null) localStorage.removeItem(USER_ID_STORAGE_KEY)
  else localStorage.setItem(USER_ID_STORAGE_KEY, String(id))
}

export function ProfilePage() {
  const { user, isAuthenticated } = useAuth()
  const [userIdInput, setUserIdInput] = useState('')

  useEffect(() => {
    const id = getStoredUserId()
    setUserIdInput(id != null ? String(id) : '')
  }, [])

  const handleUserIdBlur = () => {
    const n = userIdInput.trim() ? parseInt(userIdInput.trim(), 10) : NaN
    if (!Number.isNaN(n) && n >= 1) setStoredUserId(n)
    else setStoredUserId(null)
  }

  if (!isAuthenticated || !user) {
    return (
      <section className="page-section">
        <h2 className="page-title">Profile</h2>
        <p className="muted">Sign in to see your profile.</p>
      </section>
    )
  }

  return (
    <section className="page-section">
      <h2 className="page-title">Profile</h2>
      <div className="panel profile-panel">
        <dl className="profile-dl">
          <dt>Username</dt>
          <dd>{user.preferred_username ?? user.sub}</dd>
          {user.email && (
            <>
              <dt>Email</dt>
              <dd>{user.email}</dd>
            </>
          )}
          {user.name && (
            <>
              <dt>Name</dt>
              <dd>{user.name}</dd>
            </>
          )}
          <dt>Subject</dt>
          <dd className="muted">{user.sub}</dd>
        </dl>
        <div className="form-field" style={{ marginTop: '1rem' }}>
          <label>
            <span className="muted">Your user ID (for joining teams and creating cards)</span>
            <input
              type="number"
              min={1}
              value={userIdInput}
              onChange={(e) => setUserIdInput(e.target.value)}
              onBlur={handleUserIdBlur}
              placeholder="Optional"
            />
          </label>
        </div>
      </div>
    </section>
  )
}
