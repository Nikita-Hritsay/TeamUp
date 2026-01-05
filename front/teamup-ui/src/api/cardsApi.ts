import type { CardRequestDto, CardResponseDto, PageResponse, ResponseDto } from '../types'

const BASE_URL = 'http://localhost:8080/api/v1/cards'

async function handleResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    let message = 'Request failed'
    try {
      const data = await response.json()
      message =
        (data && (data.statusMessage || data.message || data.error)) ?? message
    } catch {
      const text = await response.text()
      if (text) message = text
    }
    throw new Error(message)
  }

  if (response.status === 204) {
    return {} as T
  }

  return response.json() as Promise<T>
}

export async function fetchCards(params: {
  page?: number
  size?: number
  ownerId?: number
  title?: string
}) {
  const url = new URL(BASE_URL)
  if (params.page !== undefined) url.searchParams.set('page', String(params.page))
  if (params.size !== undefined) url.searchParams.set('size', String(params.size))
  if (params.ownerId !== undefined)
    url.searchParams.set('ownerId', String(params.ownerId))
  if (params.title) url.searchParams.set('title', params.title)

  const response = await fetch(url.toString())
  return handleResponse<PageResponse<CardResponseDto>>(response)
}

export async function getCardById(cardId: number) {
  const url = `${BASE_URL}/fetch?cardId=${encodeURIComponent(cardId)}`
  const response = await fetch(url)
  return handleResponse<CardResponseDto>(response)
}

export async function getCardsByUser(userId: number) {
  const url = `${BASE_URL}/fetchByUser?userId=${encodeURIComponent(userId)}`
  const response = await fetch(url)
  return handleResponse<CardResponseDto[]>(response)
}

export async function createCard(payload: CardRequestDto) {
  const response = await fetch(BASE_URL, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  })
  return handleResponse<ResponseDto>(response)
}

export async function updateCard(cardId: number, payload: CardRequestDto) {
  const response = await fetch(`${BASE_URL}/${cardId}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  })
  return handleResponse<CardResponseDto>(response)
}

export async function deleteCard(cardId: number) {
  const response = await fetch(`${BASE_URL}/${cardId}`, { method: 'DELETE' })
  return handleResponse<ResponseDto>(response)
}
