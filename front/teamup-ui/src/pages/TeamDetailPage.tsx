import { useEffect, useMemo, useState } from 'react'
import { useNavigate } from '../routerShim'
import { fetchTeam, getTeamMembersByTeamId, joinTeam } from '../api/teamsApi'
import { fetchCardsByTeamId } from '../api/cardsApi'
import { getStoredUserId } from './ProfilePage'
import type {
  CardResponseDto,
  PageResponse,
  TeamMemberDto,
  TeamResponseDto,
} from '../types'

const MEMBERS_PAGE_SIZE = 10

function getTeamIdFromPath(): number | null {
  const match = window.location.pathname.match(/^\/teams\/(\d+)$/)
  if (!match) return null
  const n = parseInt(match[1], 10)
  return Number.isNaN(n) ? null : n
}

function ownerFromMembers(members?: TeamMemberDto[]): TeamMemberDto | null {
  if (!members?.length) return null
  return members.find((m) => m.role === 'CREATOR' || m.role === 'LEADER') ?? null
}

export function TeamDetailPage() {
  const navigate = useNavigate()
  const teamId = getTeamIdFromPath()

  const [team, setTeam] = useState<TeamResponseDto | null>(null)
  const [membersPage, setMembersPage] = useState<PageResponse<TeamMemberDto> | null>(null)
  const [cards, setCards] = useState<CardResponseDto[]>([])
  const [membersPageNum, setMembersPageNum] = useState(0)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [joinMessage, setJoinMessage] = useState<{ cardId?: number; ok: boolean; text: string } | null>(null)

  useEffect(() => {
    if (!teamId) {
      setError('Invalid team ID')
      setLoading(false)
      return
    }
    const load = async () => {
      setLoading(true)
      setError('')
      try {
        const [teamRes, membersRes, cardsRes] = await Promise.all([
          fetchTeam(teamId),
          getTeamMembersByTeamId(teamId, { page: membersPageNum, size: MEMBERS_PAGE_SIZE }),
          fetchCardsByTeamId(teamId),
        ])
        setTeam(teamRes)
        setMembersPage(membersRes)
        setCards(cardsRes)
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to load team.')
        setTeam(null)
        setMembersPage(null)
        setCards([])
      } finally {
        setLoading(false)
      }
    }
    load()
  }, [teamId, membersPageNum])

  const totalMembersPages = useMemo(() => membersPage?.totalPages ?? 0, [membersPage])
  const owner = useMemo(() => {
    const fromTeam = team?.teamMembers?.length ? ownerFromMembers(team.teamMembers) : null
    if (fromTeam) return fromTeam
    return membersPage?.content?.length ? ownerFromMembers(membersPage.content) : null
  }, [team?.teamMembers, membersPage?.content])

  const storedUserId = getStoredUserId()
  const canJoin = Boolean(teamId && storedUserId != null && storedUserId >= 1)

  const handleJoinApply = async (cardId?: number) => {
    if (!teamId || !storedUserId) return
    setJoinMessage(null)
    try {
      await joinTeam({
        userId: storedUserId,
        role: 'PARTICIPANT',
        teamId,
        ...(cardId != null ? { cardId } : {}),
      })
      setJoinMessage({ cardId, ok: true, text: 'Request sent.' })
      const [teamRes, membersRes, cardsRes] = await Promise.all([
        fetchTeam(teamId),
        getTeamMembersByTeamId(teamId, { page: membersPageNum, size: MEMBERS_PAGE_SIZE }),
        fetchCardsByTeamId(teamId),
      ])
      setTeam(teamRes)
      setMembersPage(membersRes)
      setCards(cardsRes)
    } catch (err) {
      setJoinMessage({
        cardId,
        ok: false,
        text: err instanceof Error ? err.message : 'Failed to join.',
      })
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
        <h2 className="page-title">Team</h2>
        <p className="muted">Loading…</p>
      </section>
    )
  }

  if (error) {
    return (
      <section className="page-section">
        <h2 className="page-title">Team</h2>
        <div className="error">{error}</div>
        <button type="button" className="btn btn-secondary" onClick={() => navigate('/')}>
          Back to Home
        </button>
      </section>
    )
  }

  return (
    <section className="page-section">
      <div className="page-actions">
        <button type="button" className="btn btn-secondary" onClick={() => navigate('/')}>
          ← Back to Home
        </button>
      </div>
      {team && (
        <>
          <div className="panel team-info-panel">
            <h2 className="team-name">{team.name}</h2>
            {team.description && (
              <p className="team-description muted">{team.description}</p>
            )}
            {owner && (
              <p className="team-owner muted">Owner: User {owner.userId}</p>
            )}
          </div>

          <div className="panel">
            <h3>Members</h3>
            {membersPage?.content.length ? (
              <>
                <ul className="list">
                  {membersPage.content.map((m) => (
                    <li key={m.id ?? `${m.userId}-${m.teamId}`} className="list-item">
                      <strong>User {m.userId}</strong>
                      <span className="muted"> · {m.role}</span>
                      {m.status && <span className="muted"> · {m.status}</span>}
                    </li>
                  ))}
                </ul>
                {totalMembersPages > 1 && (
                  <div className="pagination">
                    <button
                      type="button"
                      disabled={membersPageNum === 0}
                      onClick={() => setMembersPageNum((p) => p - 1)}
                    >
                      Previous
                    </button>
                    <span>
                      Page {membersPageNum + 1} of {totalMembersPages}
                    </span>
                    <button
                      type="button"
                      disabled={membersPageNum + 1 >= totalMembersPages}
                      onClick={() => setMembersPageNum((p) => p + 1)}
                    >
                      Next
                    </button>
                  </div>
                )}
              </>
            ) : (
              <p className="muted">No members yet.</p>
            )}
          </div>

          <div className="panel cards-panel">
            <div className="panel-header">
              <h3 style={{ margin: 0 }}>Cards (vacancies)</h3>
              <button
                type="button"
                className="btn btn-primary"
                onClick={() => navigate(`/teams/${teamId}/create-card`)}
              >
                Create card
              </button>
            </div>
            {joinMessage && (
              <div className={joinMessage.ok ? 'success' : 'error'}>{joinMessage.text}</div>
            )}
            {cards.length > 0 ? (
              <div className="cards-list">
                {cards.map((card) => (
                  <article key={card.id} className="vacancy-card">
                    <h4 className="vacancy-card-title">{card.title}</h4>
                    {card.description && (
                      <p className="vacancy-card-description muted">
                        {card.description.length > 100
                          ? `${card.description.slice(0, 100)}…`
                          : card.description}
                      </p>
                    )}
                    <div className="vacancy-card-actions">
                      <button
                        type="button"
                        className="btn btn-primary btn-sm"
                        onClick={() => navigate(`/cards/${card.id}`)}
                      >
                        View
                      </button>
                      {canJoin ? (
                        <button
                          type="button"
                          className="btn btn-secondary btn-sm"
                          onClick={() => handleJoinApply(card.id)}
                        >
                          Join / Apply
                        </button>
                      ) : (
                        <span className="muted small">Set your User ID in Profile to join</span>
                      )}
                    </div>
                  </article>
                ))}
              </div>
            ) : (
              <p className="muted">No cards yet. Create one to add vacancies.</p>
            )}
          </div>
        </>
      )}
    </section>
  )
}
