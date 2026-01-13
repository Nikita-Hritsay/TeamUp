import { useEffect, useMemo, useState } from 'react'
import type { FormEvent } from 'react'
import { useNavigate } from '../routerShim'
import { createCard, fetchCards } from '../api/cardsApi'
import type { CardRequestDto, CardResponseDto, PageResponse } from '../types'

type CardFilters = {
  ownerId?: number
  title?: string
}

export function CardsPage() {
  const navigate = useNavigate()
  const [filters, setFilters] = useState<CardFilters>({})
  const [page, setPage] = useState(0)
  const [size, setSize] = useState(10)
  const [cardsPage, setCardsPage] = useState<PageResponse<CardResponseDto> | null>(
    null,
  )
  const [cardsError, setCardsError] = useState('')
  const [isLoading, setIsLoading] = useState(false)

  const [formData, setFormData] = useState<CardRequestDto>({
    title: '',
    description: '',
    posterUrl: '',
    ownerId: 0,
    teamId: 0,
  })
  const [formError, setFormError] = useState('')
  const [formSuccess, setFormSuccess] = useState('')

  const totalPages = useMemo(() => cardsPage?.totalPages ?? 0, [cardsPage])

  useEffect(() => {
    const loadCards = async () => {
      setIsLoading(true)
      setCardsError('')
      try {
        const data = await fetchCards({
          page,
          size,
          ownerId: filters.ownerId,
          title: filters.title,
        })
        setCardsPage(data)
      } catch (err) {
        setCardsError(err instanceof Error ? err.message : 'Unable to load cards.')
        setCardsPage(null)
      } finally {
        setIsLoading(false)
      }
    }

    loadCards()
  }, [filters, page, size])

  const handleFilterSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    setPage(0)
    const formData = new FormData(event.currentTarget)
    const ownerIdInput = (formData.get('ownerId') as string | null)?.trim() ?? ''
    const titleInput = (formData.get('title') as string | null)?.trim() ?? ''
    setFilters({
      ownerId: ownerIdInput ? Number(ownerIdInput) : undefined,
      title: titleInput || undefined,
    })
  }

  const handleCreateCard = async (event: FormEvent) => {
    event.preventDefault()
    setFormError('')
    setFormSuccess('')
    if (!formData.title || !formData.ownerId || !formData.teamId) {
      setFormError('Title, Owner ID, and Team ID are required.')
      return
    }

    try {
      await createCard(formData)
      setFormSuccess('Card created successfully.')
      setFormData({
        title: '',
        description: '',
        posterUrl: '',
        ownerId: formData.ownerId,
        teamId: formData.teamId,
      })
      setPage(0)
      setFilters((current) => ({ ...current }))
    } catch (err) {
      setFormError(err instanceof Error ? err.message : 'Unable to create card.')
    }
  }

  const handleInputChange = <K extends keyof CardRequestDto>(key: K, value: string) => {
    setFormData((prev) => ({
      ...prev,
      [key]:
        key === 'ownerId' || key === 'teamId'
          ? Number(value)
          : (value as CardRequestDto[K]),
    }))
  }

  return (
    <section>
      <h2>Cards</h2>
      <form className="form inline-form" onSubmit={handleFilterSubmit}>
        <label className="form-field">
          <span>Owner ID</span>
          <input name="ownerId" type="number" min={1} defaultValue={filters.ownerId} />
        </label>
        <label className="form-field">
          <span>Title</span>
          <input name="title" type="text" defaultValue={filters.title} />
        </label>
        <label className="form-field small">
          <span>Page Size</span>
          <input
            type="number"
            min={1}
            max={50}
            value={size}
            onChange={(event) => {
              const nextSize = Number(event.target.value)
              setSize(Number.isNaN(nextSize) ? 5 : nextSize)
              setPage(0)
            }}
          />
        </label>
        <button type="submit" disabled={isLoading}>
          {isLoading ? 'Loading…' : 'Apply'}
        </button>
      </form>
      {cardsError && <div className="error">{cardsError}</div>}

      <div className="panel">
        {isLoading && <p>Loading cards…</p>}
        {!isLoading && cardsPage && (
          <>
            <ul className="list">
              {cardsPage.content.map((card) => (
                <li
                  key={card.id}
                  className="list-item"
                  style={{ cursor: 'pointer' }}
                  onClick={() => navigate(`/cards/${card.id}`)}
                >
                  <div className="list-title">
                    <strong>{card.title}</strong>
                    <span className="muted">Card #{card.id}</span>
                  </div>
                  {card.description && (
                    <p className="muted">
                      {card.description.length > 100
                        ? `${card.description.substring(0, 100)}...`
                        : card.description}
                    </p>
                  )}
                  <p className="muted">
                    Owner: {card.ownerId} • Team: {card.teamId}
                  </p>
                </li>
              ))}
            </ul>

            {totalPages > 0 && (
              <div className="pagination">
                <button
                  type="button"
                  onClick={() => setPage((prev) => Math.max(prev - 1, 0))}
                  disabled={page === 0}
                >
                  Previous
                </button>
                <span>
                  Page {page + 1} of {totalPages}
                </span>
                <button
                  type="button"
                  onClick={() =>
                    setPage((prev) =>
                      cardsPage.totalPages ? Math.min(prev + 1, totalPages - 1) : prev,
                    )
                  }
                  disabled={page + 1 >= totalPages}
                >
                  Next
                </button>
              </div>
            )}
          </>
        )}
        {!isLoading && !cardsPage && <p>No cards found.</p>}
      </div>

      <div className="panel">
        <h3>Create Card</h3>
        <form className="form" onSubmit={handleCreateCard}>
          <label className="form-field">
            <span>Title</span>
            <input
              type="text"
              value={formData.title}
              onChange={(event) => handleInputChange('title', event.target.value)}
              required
              minLength={3}
            />
          </label>
          <label className="form-field">
            <span>Description</span>
            <textarea
              value={formData.description}
              onChange={(event) => handleInputChange('description', event.target.value)}
              maxLength={500}
            />
          </label>
          <label className="form-field">
            <span>Poster URL</span>
            <input
              type="url"
              value={formData.posterUrl}
              onChange={(event) => handleInputChange('posterUrl', event.target.value)}
              placeholder="https://example.com/image.jpg"
            />
          </label>
          <label className="form-field">
            <span>Owner ID</span>
            <input
              type="number"
              min={1}
              value={formData.ownerId || ''}
              onChange={(event) => handleInputChange('ownerId', event.target.value)}
              required
            />
          </label>
          <label className="form-field">
            <span>Team ID</span>
            <input
              type="number"
              min={1}
              value={formData.teamId || ''}
              onChange={(event) => handleInputChange('teamId', event.target.value)}
              required
            />
          </label>
          <button type="submit">Create</button>
        </form>
        {formError && <div className="error">{formError}</div>}
        {formSuccess && <div className="success">{formSuccess}</div>}
      </div>
    </section>
  )
}
