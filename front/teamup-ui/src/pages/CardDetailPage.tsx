import { useEffect, useState } from 'react'
import { useNavigate } from '../routerShim'
import { getCardById } from '../api/cardsApi'
import type { CardResponseDto } from '../types'

export function CardDetailPage() {
  const navigate = useNavigate()
  const [card, setCard] = useState<CardResponseDto | null>(null)
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState('')

  // Get cardId from URL
  const cardId = parseInt(window.location.pathname.split('/cards/')[1] || '', 10)

  useEffect(() => {
    if (!cardId || isNaN(cardId)) {
      setError('Invalid card ID')
      return
    }

    const loadCard = async () => {
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
    }

    loadCard()
  }, [cardId])

  if (isLoading) {
    return (
      <section>
        <h2>Card Details</h2>
        <div className="panel">
          <p>Loading card details…</p>
        </div>
      </section>
    )
  }

  if (error) {
    return (
      <section>
        <h2>Card Details</h2>
        <div className="panel">
          <div className="error">{error}</div>
          <button type="button" onClick={() => navigate('/cards')}>
            Back to Cards
          </button>
        </div>
      </section>
    )
  }

  if (!card) {
    return (
      <section>
        <h2>Card Details</h2>
        <div className="panel">
          <p>Card not found.</p>
          <button type="button" onClick={() => navigate('/cards')}>
            Back to Cards
          </button>
        </div>
      </section>
    )
  }

  return (
    <section>
      <div style={{ marginBottom: '1rem' }}>
        <button type="button" onClick={() => navigate('/cards')}>
          ← Back to Cards
        </button>
      </div>
      <h2>{card.title}</h2>
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
        <div className="muted" style={{ borderTop: '1px solid #ddd', paddingTop: '1rem' }}>
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
    </section>
  )
}

