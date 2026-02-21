import { useEffect, useMemo, useState } from 'react'
import type { FormEvent } from 'react'
import { useNavigate } from '../routerShim'
import { createTeam, listTeams } from '../api/teamsApi'
import type { TeamRequestDto, TeamResponseDto, PageResponse } from '../types'

const PAGE_SIZE = 10

export function TeamsPage() {
  const navigate = useNavigate()
  const [teamForm, setTeamForm] = useState<TeamRequestDto>({
    name: '',
    description: '',
  })
  const [teamMessage, setTeamMessage] = useState('')
  const [teamError, setTeamError] = useState('')

  const [page, setPage] = useState(0)
  const [teamsPage, setTeamsPage] = useState<PageResponse<TeamResponseDto> | null>(null)
  const [teamsError, setTeamsError] = useState('')
  const [teamsLoading, setTeamsLoading] = useState(false)

  useEffect(() => {
    const load = async () => {
      setTeamsLoading(true)
      setTeamsError('')
      try {
        const data = await listTeams({ page, size: PAGE_SIZE })
        setTeamsPage(data)
      } catch (err) {
        setTeamsError(err instanceof Error ? err.message : 'Unable to load teams.')
        setTeamsPage(null)
      } finally {
        setTeamsLoading(false)
      }
    }
    load()
  }, [page])

  const totalPages = useMemo(() => teamsPage?.totalPages ?? 0, [teamsPage])

  const handleCreateTeam = async (event: FormEvent) => {
    event.preventDefault()
    setTeamMessage('')
    setTeamError('')
    if (!teamForm.name) {
      setTeamError('Team name is required.')
      return
    }
    try {
      await createTeam(teamForm)
      setTeamMessage('Team created successfully.')
      setTeamForm({ name: '', description: '' })
      setPage(0)
      const data = await listTeams({ page: 0, size: PAGE_SIZE })
      setTeamsPage(data)
    } catch (err) {
      setTeamError(err instanceof Error ? err.message : 'Unable to create team.')
    }
  }

  return (
    <section>
      <h2>My Teams</h2>
      <div className="panel">
        <h3>Create Team</h3>
        <form className="form" onSubmit={handleCreateTeam}>
          <label className="form-field">
            <span>Name</span>
            <input
              type="text"
              value={teamForm.name}
              onChange={(event) =>
                setTeamForm((prev) => ({ ...prev, name: event.target.value }))
              }
              required
            />
          </label>
          <label className="form-field">
            <span>Description</span>
            <textarea
              value={teamForm.description ?? ''}
              onChange={(event) =>
                setTeamForm((prev) => ({ ...prev, description: event.target.value }))
              }
            />
          </label>
          <button type="submit">Create</button>
        </form>
        {teamError && <div className="error">{teamError}</div>}
        {teamMessage && <div className="success">{teamMessage}</div>}
      </div>

      <div className="panel">
        <h3>Your teams</h3>
        {teamsLoading && <p>Loading teams…</p>}
        {teamsError && <div className="error">{teamsError}</div>}
        {!teamsLoading && teamsPage && (
          <>
            {teamsPage.content.length === 0 ? (
              <p className="muted">No teams yet. Create one above.</p>
            ) : (
              <>
                <ul className="list">
                  {teamsPage.content.map((team) => (
                    <li
                      key={team.id}
                      className="list-item"
                      style={{ cursor: 'pointer' }}
                      onClick={() => navigate(`/teams/${team.id}`)}
                    >
                      <div className="list-title">
                        <strong>{team.name}</strong>
                        <span className="muted">Team #{team.id}</span>
                      </div>
                      {team.description && (
                        <p className="muted">
                          {team.description.length > 100
                            ? `${team.description.substring(0, 100)}...`
                            : team.description}
                        </p>
                      )}
                    </li>
                  ))}
                </ul>
                {totalPages > 1 && (
                  <div className="pagination">
                    <button
                      type="button"
                      onClick={() => setPage((p) => Math.max(0, p - 1))}
                      disabled={page === 0}
                    >
                      Previous
                    </button>
                    <span>
                      Page {page + 1} of {totalPages}
                    </span>
                    <button
                      type="button"
                      onClick={() => setPage((p) => Math.min(totalPages - 1, p + 1))}
                      disabled={page + 1 >= totalPages}
                    >
                      Next
                    </button>
                  </div>
                )}
              </>
            )}
          </>
        )}
      </div>
    </section>
  )
}
