import { useState, useEffect, type FormEvent } from 'react'
import { useNavigate } from '../routerShim'
import { createCard } from '../api/cardsApi'
import { fetchTeam } from '../api/teamsApi'
import { getStoredUserId } from './ProfilePage'
import type { CardRequestDto, TeamResponseDto } from '../types'

function getTeamIdFromPath(): number | null {
  const match = window.location.pathname.match(/^\/teams\/(\d+)\/create-card/)
  if (!match) return null
  const n = parseInt(match[1], 10)
  return Number.isNaN(n) ? null : n
}

export function CreateCardPage() {
  const navigate = useNavigate()
  const teamId = getTeamIdFromPath()
  const [team, setTeam] = useState<TeamResponseDto | null>(null)
  const [form, setForm] = useState<CardRequestDto>({
    title: '',
    description: '',
    posterUrl: '',
    ownerId: 0,
    teamId: teamId ?? 0,
  })
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [submitting, setSubmitting] = useState(false)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    if (!teamId) {
      setLoading(false)
      return
    }
    const stored = getStoredUserId()
    setForm((f) => ({ ...f, teamId, ownerId: stored ?? 0 }))
    let cancelled = false
    fetchTeam(teamId)
      .then((t) => { if (!cancelled) setTeam(t) })
      .catch(() => { if (!cancelled) setTeam(null) })
      .finally(() => { if (!cancelled) setLoading(false) })
    return () => { cancelled = true }
  }, [teamId])

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault()
    setError('')
    setSuccess('')
    if (!teamId) {
      setError('Invalid team.')
      return
    }
    if (!form.title?.trim()) {
      setError('Title is required.')
      return
    }
    const ownerId = form.ownerId
    if (!ownerId || ownerId < 1) {
      setError('Owner ID is required (your numeric user ID).')
      return
    }
    setSubmitting(true)
    try {
      await createCard({
        title: form.title.trim(),
        description: form.description?.trim() || undefined,
        posterUrl: form.posterUrl?.trim() || undefined,
        ownerId,
        teamId,
      })
      setSuccess('Card created.')
      setTimeout(() => navigate(`/teams/${teamId}`), 800)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to create card.')
    } finally {
      setSubmitting(false)
    }
  }

  if (!teamId) {
    return (
      <section className="page-section">
        <div className="error">Invalid team.</div>
        <button type="button" className="btn btn-secondary" onClick={() => navigate('/')}>
          Back to Home
        </button>
      </section>
    )
  }

  if (loading) {
    return (
      <section className="page-section">
        <h2 className="page-title">Create Card</h2>
        <p className="muted">Loading…</p>
      </section>
    )
  }

  return (
    <section className="page-section">
      <h2 className="page-title">Create Card</h2>
      {team && <p className="muted" style={{ marginBottom: '1rem' }}>Team: {team.name}</p>}
      <div className="panel">
        <form className="form" onSubmit={handleSubmit}>
          <label className="form-field">
            <span>Title</span>
            <input
              type="text"
              value={form.title}
              onChange={(e) => setForm((f) => ({ ...f, title: e.target.value }))}
              required
              minLength={3}
              maxLength={100}
              placeholder="Vacancy or role title"
            />
          </label>
          <label className="form-field">
            <span>Description</span>
            <textarea
              value={form.description ?? ''}
              onChange={(e) => setForm((f) => ({ ...f, description: e.target.value }))}
              maxLength={500}
              placeholder="Short description (optional)"
            />
          </label>
          <label className="form-field">
            <span>Poster URL</span>
            <input
              type="url"
              value={form.posterUrl ?? ''}
              onChange={(e) => setForm((f) => ({ ...f, posterUrl: e.target.value }))}
              placeholder="https://example.com/image.jpg"
            />
          </label>
          <label className="form-field">
            <span>Owner ID (your user ID)</span>
            <input
              type="number"
              min={1}
              value={form.ownerId || ''}
              onChange={(e) =>
                setForm((f) => ({ ...f, ownerId: parseInt(e.target.value, 10) || 0 }))
              }
              required
            />
          </label>
          {error && <div className="error">{error}</div>}
          {success && <div className="success">{success}</div>}
          <div className="form-actions">
            <button type="submit" disabled={submitting}>
              {submitting ? 'Creating…' : 'Create Card'}
            </button>
            <button
              type="button"
              className="btn btn-secondary"
              onClick={() => navigate(`/teams/${teamId}`)}
            >
              Cancel
            </button>
          </div>
        </form>
      </div>
    </section>
  )
}
