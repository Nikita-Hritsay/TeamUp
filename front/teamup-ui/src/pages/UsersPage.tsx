import { useState } from 'react'
import type { FormEvent } from 'react'
import { fetchUser } from '../api/usersApi'
import type { CardResponseDto, UserDto } from '../types'

export function UsersPage() {
  const [userId, setUserId] = useState('')
  const [user, setUser] = useState<UserDto | null>(null)
  const [error, setError] = useState('')
  const [isLoading, setIsLoading] = useState(false)

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault()
    const parsedId = Number(userId)
    if (!parsedId) {
      setError('Please enter a valid numeric user ID.')
      setUser(null)
      return
    }

    setIsLoading(true)
    setError('')
    setUser(null)
    try {
      const data = await fetchUser(parsedId)
      setUser(data)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Unable to fetch user.')
    } finally {
      setIsLoading(false)
    }
  }

  const renderCards = (cards?: CardResponseDto[]) => {
    if (!cards || cards.length === 0) {
      return <p>No cards found for this user.</p>
    }

    return (
      <ul className="list">
        {cards.map((card) => (
          <li key={card.id} className="list-item">
            <strong>{card.title}</strong>
            {card.description && <span className="muted"> — {card.description}</span>}
          </li>
        ))}
      </ul>
    )
  }

  return (
    <section className="page-section">
      <h2 className="page-title">Users</h2>
      <form className="form" onSubmit={handleSubmit}>
        <label className="form-field">
          <span>User ID</span>
          <input
            type="number"
            value={userId}
            onChange={(event) => setUserId(event.target.value)}
            required
            min={1}
          />
        </label>
        <button type="submit" disabled={isLoading}>
          {isLoading ? 'Loading…' : 'Fetch User'}
        </button>
      </form>

      {error && <div className="error">{error}</div>}

      {user && (
        <div className="panel profile-panel">
          <h3>
            {user.firstName} {user.lastName}
          </h3>
          <p className="muted">
            Email: {user.email} • Mobile: {user.mobileNumber}
          </p>

          {user.roles && user.roles.length > 0 && (
            <div>
              <h4>Roles</h4>
              <ul className="list inline">
                {user.roles.map((role) => (
                  <li key={role.roleName} className="pill">
                    {role.roleName}
                  </li>
                ))}
              </ul>
            </div>
          )}

          <div>
            <h4>Cards</h4>
            {renderCards(user.cards)}
          </div>
        </div>
      )}
    </section>
  )
}
