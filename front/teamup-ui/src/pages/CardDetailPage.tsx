import { useCallback, useEffect, useState } from 'react'
import { useNavigate } from '../routerShim'
import { getCardById } from '../api/cardsApi'
import { getTeamMembers, joinTeam, updateMemberStatus } from '../api/teamsApi'
import { getStoredUserId } from './ProfilePage'
import type { CardResponseDto, TeamMemberDto } from '../types'

const MEMBERS_PAGE_SIZE = 50

export function CardDetailPage() {
  const navigate = useNavigate()
  const [card, setCard] = useState<CardResponseDto | null>(null)
  const [membersPage, setMembersPage] = useState<{ content: TeamMemberDto[] } | null>(null)
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState('')
  const [joinMessage, setJoinMessage] = useState<{ ok: boolean; text: string } | null>(null)
  const [statusMessage, setStatusMessage] = useState<string | null>(null)

  // Get cardId from URL
  const cardId = parseInt(window.location.pathname.split('/cards/')[1] || '', 10)

  const loadCard = useCallback(async () => {
    if (!cardId || isNaN(cardId)) return
    setIsLoading(true)
    setError('')
    try {
      const data = await getCardById(cardId)
      setCard(data)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Unable to load card.')
      setCard(null)
    } finally {
      setIsLoading(false)
    }
  }, [cardId])

  const loadMembers = useCallback(async () => {
    if (!cardId || isNaN(cardId)) return
    try {
      const res = await getTeamMembers(cardId, { page: 0, size: MEMBERS_PAGE_SIZE })
      setMembersPage(res)
    } catch {
      setMembersPage(null)
    }
  }, [cardId])

  useEffect(() => {
    if (!cardId || isNaN(cardId)) {
      setError('Invalid card ID')
      return
    }
    loadCard()
  }, [cardId, loadCard])

  useEffect(() => {
    if (!cardId || isNaN(cardId) || !card) return
    loadMembers()
  }, [cardId, card, loadMembers])

  const storedUserId = getStoredUserId()
  const canJoin = Boolean(cardId && storedUserId != null && storedUserId >= 1)

  const handleJoinApply = async () => {
    if (!storedUserId) return
    setJoinMessage(null)
    try {
      await joinTeam({ userId: storedUserId, cardId, teamId: card?.teamId ?? 0, role: 'PARTICIPANT' })
      setJoinMessage({ ok: true, text: 'Request sent. Wait for a team member to accept.' })
      await loadMembers()
    } catch (err) {
      setJoinMessage({
        ok: false,
        text: err instanceof Error ? err.message : 'Failed to send request.',
      })
    }
  }

  const handleUpdateStatus = async (userId: number, status: 'JOINED' | 'REJECTED') => {
    setStatusMessage(null)
    try {
      await updateMemberStatus(cardId, userId, status)
      setStatusMessage(status === 'JOINED' ? 'Accepted.' : 'Rejected.')
      await loadMembers()
    } catch (err) {
      setStatusMessage(err instanceof Error ? err.message : 'Action failed.')
    }
  }

  const pendingMembers = membersPage?.content?.filter((m) => m.status === 'PENDING') ?? []

  if (isLoading) {
    return (
      <section className="page-section">
        <h2 className="page-title">Card Details</h2>
        <div className="panel">
          <p className="muted">Loading card details…</p>
        </div>
      </section>
    )
  }

  const backTo = card?.teamId ? `/teams/${card.teamId}` : '/'

  if (error) {
    return (
      <section className="page-section">
        <h2 className="page-title">Card Details</h2>
        <div className="panel">
          <div className="error">{error}</div>
          <button type="button" className="btn btn-secondary" onClick={() => navigate('/')}>
            Back to Home
          </button>
        </div>
      </section>
    )
  }

  if (!card) {
    return (
      <section className="page-section">
        <h2 className="page-title">Card Details</h2>
        <div className="panel">
          <p>Card not found.</p>
          <button type="button" className="btn btn-secondary" onClick={() => navigate('/')}>
            Back to Home
          </button>
        </div>
      </section>
    )
  }

  return (
    <section className="page-section">
      <div className="page-actions">
        <button type="button" className="btn btn-secondary" onClick={() => navigate(backTo)}>
          ← Back to {card.teamId ? 'Team' : 'Home'}
        </button>
      </div>
      <h2 className="page-title">{card.title}</h2>
      <div className="panel">
        <div style={{ marginBottom: '1rem' }}>
          {card.posterUrl && (
            <img
              src={card.posterUrl}
              alt={card.title}
              style={{ maxWidth: '100%', height: 'auto', marginBottom: '1rem' }}
            />
          )}
          <div>
            <p>
              <strong>Description:</strong>
            </p>
            <p>{card.description || 'No description provided.'}</p>
          </div>
        </div>
        {canJoin ? (
          <div style={{ marginBottom: '1rem' }}>
            <button type="button" className="btn btn-primary" onClick={handleJoinApply}>
              Join / Apply as team member
            </button>
            {joinMessage && (
              <div className={joinMessage.ok ? 'success' : 'error'} style={{ marginTop: '0.5rem' }}>
                {joinMessage.text}
              </div>
            )}
          </div>
        ) : (
          <p className="muted small" style={{ marginBottom: '1rem' }}>
            Set your User ID in Profile to apply for this card.
          </p>
        )}
        <div className="muted card-meta">
          <p>
            <strong>Card ID:</strong> {card.id}
          </p>
          <p>
            <strong>Owner ID:</strong> {card.ownerId}
          </p>
          <p>
            <strong>Team ID:</strong> {card.teamId}
          </p>
          {card.createdAt && (
            <p>
              <strong>Created:</strong> {new Date(card.createdAt).toLocaleString()}
              {card.createdBy && ` by ${card.createdBy}`}
            </p>
          )}
          {card.updatedAt && (
            <p>
              <strong>Updated:</strong> {new Date(card.updatedAt).toLocaleString()}
              {card.updatedBy && ` by ${card.updatedBy}`}
            </p>
          )}
        </div>
      </div>

      <div className="panel">
        <h3>Members &amp; requests</h3>
        {statusMessage && (
          <div className="success" style={{ marginBottom: '0.5rem' }}>
            {statusMessage}
          </div>
        )}
        {pendingMembers.length > 0 && (
          <>
            <h4 className="muted" style={{ marginTop: '0.5rem' }}>Pending requests</h4>
            <ul className="list">
              {pendingMembers.map((m) => (
                <li key={m.id ?? m.userId} className="list-item" style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', flexWrap: 'wrap' }}>
                  <strong>User {m.userId}</strong>
                  <span className="muted">· PENDING</span>
                  <button
                    type="button"
                    className="btn btn-primary btn-sm"
                    onClick={() => handleUpdateStatus(m.userId, 'JOINED')}
                  >
                    Accept
                  </button>
                  <button
                    type="button"
                    className="btn btn-secondary btn-sm"
                    onClick={() => handleUpdateStatus(m.userId, 'REJECTED')}
                  >
                    Reject
                  </button>
                </li>
              ))}
            </ul>
          </>
        )}
        {membersPage?.content?.filter((m) => m.status === 'JOINED').length ? (
          <>
            <h4 className="muted" style={{ marginTop: '1rem' }}>Members (joined)</h4>
            <ul className="list">
              {membersPage.content
                .filter((m) => m.status === 'JOINED')
                .map((m) => (
                  <li key={m.id ?? m.userId} className="list-item">
                    <strong>User {m.userId}</strong>
                    <span className="muted"> · {m.role ?? '—'}</span>
                  </li>
                ))}
            </ul>
          </>
        ) : pendingMembers.length === 0 && (
          <p className="muted">No members or pending requests for this card.</p>
        )}
      </div>
    </section>
  )
}

