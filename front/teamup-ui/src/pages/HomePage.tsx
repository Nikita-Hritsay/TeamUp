import { useEffect, useState } from 'react'
import { useNavigate } from '../routerShim'
import { listAllTeams } from '../api/teamsApi'
import type { TeamMemberDto, TeamResponseDto } from '../types'

function ownerFromMembers(members?: TeamMemberDto[]): string | null {
  if (!members?.length) return null
  const creator = members.find((m) => m.role === 'CREATOR' || m.role === 'LEADER')
  return creator ? `User ${creator.userId}` : null
}

export function HomePage() {
  const navigate = useNavigate()
  const [teams, setTeams] = useState<TeamResponseDto[]>([])
  const [error, setError] = useState('')
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    let cancelled = false
    const load = async () => {
      setError('')
      try {
        const data = await listAllTeams()
        if (!cancelled) setTeams(data)
      } catch (err) {
        if (!cancelled) {
          setError(err instanceof Error ? err.message : 'Unable to load teams.')
          setTeams([])
        }
      } finally {
        if (!cancelled) setIsLoading(false)
      }
    }
    load()
    return () => { cancelled = true }
  }, [])

  if (isLoading) {
    return (
      <section className="page-section">
        <h2 className="page-title">Teams</h2>
        <p className="muted">Loading teams…</p>
      </section>
    )
  }

  return (
    <section className="page-section">
      <h2 className="page-title">Teams</h2>
      {error && <div className="error">{error}</div>}
      {!error && teams.length === 0 && (
        <p className="muted">No teams yet. Create one to get started.</p>
      )}
      {!error && teams.length > 0 && (
        <div className="teams-grid">
          {teams.map((team) => {
            const owner = ownerFromMembers(team.teamMembers)
            return (
              <article key={team.id} className="team-card">
                <h3 className="team-card-title">{team.name}</h3>
                {team.description && (
                  <p className="team-card-description">
                    {team.description.length > 120
                      ? `${team.description.slice(0, 120)}…`
                      : team.description}
                  </p>
                )}
                {owner && (
                  <p className="team-card-meta muted">Owner: {owner}</p>
                )}
                <button
                  type="button"
                  className="btn btn-primary"
                  onClick={() => navigate(`/teams/${team.id}`)}
                >
                  View Team
                </button>
              </article>
            )
          })}
        </div>
      )}
    </section>
  )
}
