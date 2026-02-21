import { useEffect, useMemo, useState } from 'react'
import { useNavigate } from '../routerShim'
import { listTeams } from '../api/teamsApi'
import type { PageResponse, TeamResponseDto } from '../types'

const PAGE_SIZE = 10

export function AllTeamsPage() {
  const navigate = useNavigate()
  const [page, setPage] = useState(0)
  const [teamsPage, setTeamsPage] = useState<PageResponse<TeamResponseDto> | null>(null)
  const [error, setError] = useState('')
  const [isLoading, setIsLoading] = useState(false)

  useEffect(() => {
    const load = async () => {
      setIsLoading(true)
      setError('')
      try {
        const data = await listTeams({ page, size: PAGE_SIZE })
        setTeamsPage(data)
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Unable to load teams.')
        setTeamsPage(null)
      } finally {
        setIsLoading(false)
      }
    }
    load()
  }, [page])

  const totalPages = useMemo(() => teamsPage?.totalPages ?? 0, [teamsPage])

  return (
    <section>
      <h2>All Teams</h2>
      {error && <div className="error">{error}</div>}
      {isLoading && <p>Loading teams…</p>}
      {!isLoading && teamsPage && (
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
                    {team.description.length > 120
                      ? `${team.description.substring(0, 120)}...`
                      : team.description}
                  </p>
                )}
              </li>
            ))}
          </ul>
          {totalPages > 0 && (
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
      {!isLoading && !teamsPage && !error && <p className="muted">No teams found.</p>}
    </section>
  )
}
