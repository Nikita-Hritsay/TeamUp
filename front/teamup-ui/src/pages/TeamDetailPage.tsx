import { useEffect, useMemo, useState } from 'react'
import type { FormEvent } from 'react'
import { useNavigate } from '../routerShim'
import {
  fetchTeam,
  getTeamMembersByTeamId,
} from '../api/teamsApi'
import { fetchCards, createCard } from '../api/cardsApi'
import type {
  CardRequestDto,
  CardResponseDto,
  PageResponse,
  TeamMemberDto,
  TeamResponseDto,
} from '../types'

const MEMBERS_PAGE_SIZE = 10
const CARDS_PAGE_SIZE = 10

function getTeamIdFromPath(): number | null {
  const match = window.location.pathname.match(/^\/teams\/(\d+)/)
  if (!match) return null
  const n = parseInt(match[1], 10)
  return Number.isNaN(n) ? null : n
}

export function TeamDetailPage() {
  const navigate = useNavigate()
  const teamId = getTeamIdFromPath()

  const [team, setTeam] = useState<TeamResponseDto | null>(null)
  const [membersPage, setMembersPage] = useState<PageResponse<TeamMemberDto> | null>(null)
  const [cardsPage, setCardsPage] = useState<PageResponse<CardResponseDto> | null>(null)
  const [membersPageNum, setMembersPageNum] = useState(0)
  const [cardsPageNum, setCardsPageNum] = useState(0)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  const [showCreateCard, setShowCreateCard] = useState(false)
  const [cardForm, setCardForm] = useState<CardRequestDto>({
    title: '',
    description: '',
    posterUrl: '',
    ownerId: 0,
    teamId: teamId ?? 0,
  })
  const [cardMessage, setCardMessage] = useState('')
  const [cardError, setCardError] = useState('')

  useEffect(() => {
    if (!teamId) {
      setError('Invalid team ID')
      setLoading(false)
      return
    }
    setCardForm((f) => ({ ...f, teamId }))
  }, [teamId])

  useEffect(() => {
    if (!teamId) return
    const load = async () => {
      setLoading(true)
      setError('')
      try {
        const [teamRes, membersRes, cardsRes] = await Promise.all([
          fetchTeam(teamId),
          getTeamMembersByTeamId(teamId, {
            page: membersPageNum,
            size: MEMBERS_PAGE_SIZE,
          }),
          fetchCards({
            teamId,
            page: cardsPageNum,
            size: CARDS_PAGE_SIZE,
          }),
        ])
        setTeam(teamRes)
        setMembersPage(membersRes)
        setCardsPage(cardsRes)
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to load team.')
        setTeam(null)
        setMembersPage(null)
        setCardsPage(null)
      } finally {
        setLoading(false)
      }
    }
    load()
  }, [teamId, membersPageNum, cardsPageNum])

  const totalMembersPages = useMemo(() => membersPage?.totalPages ?? 0, [membersPage])
  const totalCardsPages = useMemo(() => cardsPage?.totalPages ?? 0, [cardsPage])

  const handleCreateCard = async (e: FormEvent) => {
    e.preventDefault()
    setCardError('')
    setCardMessage('')
    if (!teamId || !cardForm.title) {
      setCardError('Title is required.')
      return
    }
    const ownerId = cardForm.ownerId
    if (!ownerId || ownerId < 1) {
      setCardError('Owner ID is required (numeric user ID from the Users service).')
      return
    }
    try {
      await createCard({
        ...cardForm,
        teamId,
        ownerId,
      })
      setCardMessage('Card created.')
      setCardForm({
        title: '',
        description: '',
        posterUrl: '',
        ownerId: cardForm.ownerId || ownerId,
        teamId,
      })
      setShowCreateCard(false)
      setCardsPageNum(0)
      const cardsRes = await fetchCards({ teamId, page: 0, size: CARDS_PAGE_SIZE })
      setCardsPage(cardsRes)
    } catch (err) {
      setCardError(err instanceof Error ? err.message : 'Failed to create card.')
    }
  }

  if (!teamId) {
    return (
      <section>
        <div className="error">Invalid team.</div>
        <button type="button" onClick={() => navigate('/teams')}>
          Back to Teams
        </button>
      </section>
    )
  }

  if (loading) {
    return (
      <section>
        <h2>Team</h2>
        <p>Loading…</p>
      </section>
    )
  }

  if (error) {
    return (
      <section>
        <h2>Team</h2>
        <div className="error">{error}</div>
        <button type="button" onClick={() => navigate('/teams')}>
          Back to Teams
        </button>
      </section>
    )
  }

  return (
    <section>
      <div style={{ marginBottom: '1rem' }}>
        <button type="button" onClick={() => navigate('/teams')}>
          ← Back to Teams
        </button>
      </div>
      {team && (
        <>
          <h2>{team.name}</h2>
          {team.description && (
            <p className="muted" style={{ marginBottom: '1rem' }}>
              {team.description}
            </p>
          )}

          <div className="panel">
            <h3>Team Members</h3>
            {membersPage?.content.length ? (
              <>
                <ul className="list">
                  {membersPage.content.map((m) => (
                    <li key={m.id ?? `${m.userId}-${m.teamId}`} className="list-item">
                      <strong>User {m.userId}</strong>
                      <span className="muted"> · {m.role}</span>
                      {m.status && (
                        <span className="muted"> · {m.status}</span>
                      )}
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

          <div className="panel">
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '0.5rem' }}>
              <h3 style={{ margin: 0 }}>Cards</h3>
              <button type="button" onClick={() => setShowCreateCard((v) => !v)}>
                {showCreateCard ? 'Cancel' : 'Create new card'}
              </button>
            </div>
            {showCreateCard && (
              <form className="form" onSubmit={handleCreateCard} style={{ marginTop: '1rem' }}>
                <label className="form-field">
                  <span>Title</span>
                  <input
                    value={cardForm.title}
                    onChange={(e) => setCardForm((f) => ({ ...f, title: e.target.value }))}
                    required
                    minLength={3}
                  />
                </label>
                <label className="form-field">
                  <span>Description</span>
                  <textarea
                    value={cardForm.description ?? ''}
                    onChange={(e) => setCardForm((f) => ({ ...f, description: e.target.value }))}
                  />
                </label>
                <label className="form-field">
                  <span>Owner ID</span>
                  <input
                    type="number"
                    min={1}
                    value={cardForm.ownerId || ''}
                    onChange={(e) =>
                      setCardForm((f) => ({ ...f, ownerId: parseInt(e.target.value, 10) || 0 }))
                    }
                    required
                  />
                </label>
                {cardError && <div className="error">{cardError}</div>}
                {cardMessage && <div className="success">{cardMessage}</div>}
                <button type="submit">Create card</button>
              </form>
            )}
            {cardsPage?.content.length ? (
              <>
                <ul className="list" style={{ marginTop: '1rem' }}>
                  {cardsPage.content.map((card) => (
                    <li
                      key={card.id}
                      className="list-item"
                      style={{ cursor: 'pointer' }}
                      onClick={() => navigate(`/cards/${card.id}`)}
                    >
                      <strong>{card.title}</strong>
                      <span className="muted"> · Card #{card.id}</span>
                      {card.description && (
                        <p className="muted" style={{ margin: '0.25rem 0 0' }}>
                          {card.description.slice(0, 80)}
                          {card.description.length > 80 ? '…' : ''}
                        </p>
                      )}
                    </li>
                  ))}
                </ul>
                {totalCardsPages > 1 && (
                  <div className="pagination">
                    <button
                      type="button"
                      disabled={cardsPageNum === 0}
                      onClick={() => setCardsPageNum((p) => p - 1)}
                    >
                      Previous
                    </button>
                    <span>
                      Page {cardsPageNum + 1} of {totalCardsPages}
                    </span>
                    <button
                      type="button"
                      disabled={cardsPageNum + 1 >= totalCardsPages}
                      onClick={() => setCardsPageNum((p) => p + 1)}
                    >
                      Next
                    </button>
                  </div>
                )}
              </>
            ) : (
              <p className="muted" style={{ marginTop: '1rem' }}>
                No cards yet. Create one above.
              </p>
            )}
          </div>
        </>
      )}
    </section>
  )
}
