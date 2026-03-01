import { useState, type FormEvent } from 'react'
import { useNavigate } from '../routerShim'
import { createTeam } from '../api/teamsApi'
import type { TeamRequestDto } from '../types'

export function CreateTeamPage() {
  const navigate = useNavigate()
  const [form, setForm] = useState<TeamRequestDto>({ name: '', description: '' })
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [submitting, setSubmitting] = useState(false)

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault()
    setError('')
    setSuccess('')
    if (!form.name?.trim()) {
      setError('Team name is required.')
      return
    }
    setSubmitting(true)
    try {
      await createTeam({ name: form.name.trim(), description: form.description?.trim() })
      setSuccess('Team created successfully.')
      setForm({ name: '', description: '' })
      setTimeout(() => navigate('/'), 1000)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to create team.')
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <section className="page-section">
      <h2 className="page-title">Create Team</h2>
      <div className="panel">
        <form className="form" onSubmit={handleSubmit}>
          <label className="form-field">
            <span>Name</span>
            <input
              type="text"
              value={form.name}
              onChange={(e) => setForm((f) => ({ ...f, name: e.target.value }))}
              required
              minLength={1}
              placeholder="Team name"
            />
          </label>
          <label className="form-field">
            <span>Description</span>
            <textarea
              value={form.description ?? ''}
              onChange={(e) => setForm((f) => ({ ...f, description: e.target.value }))}
              placeholder="Short description (optional)"
            />
          </label>
          {error && <div className="error">{error}</div>}
          {success && <div className="success">{success}</div>}
          <div className="form-actions">
            <button type="submit" disabled={submitting}>
              {submitting ? 'Creating…' : 'Create Team'}
            </button>
            <button
              type="button"
              className="btn btn-secondary"
              onClick={() => navigate('/')}
            >
              Cancel
            </button>
          </div>
        </form>
      </div>
    </section>
  )
}
