import type { CardRequestDto, CardResponseDto, PageResponse, ResponseDto } from '../types'
import { fetchWithAuth, handleResponse } from './client'

const BASE_URL = import.meta.env.VITE_CARDS_API_URL ?? 'http://localhost:8081/api/v1/cards'

export async function fetchCards(params: {
  page?: number
  size?: number
  ownerId?: number
  title?: string
  teamId?: number
}) {
  const url = new URL(BASE_URL)
  if (params.page !== undefined) url.searchParams.set('page', String(params.page))
  if (params.size !== undefined) url.searchParams.set('size', String(params.size))
  if (params.ownerId !== undefined)
    url.searchParams.set('ownerId', String(params.ownerId))
  if (params.title) url.searchParams.set('title', params.title)
  if (params.teamId !== undefined) url.searchParams.set('teamId', String(params.teamId))

  const response = await fetchWithAuth(url.toString())
  return handleResponse<PageResponse<CardResponseDto>>(response)
}

export async function getCardById(cardId: number) {
  const url = `${BASE_URL}/fetch?cardId=${encodeURIComponent(cardId)}`
  const response = await fetchWithAuth(url)
  return handleResponse<CardResponseDto>(response)
}

export async function getCardsByUser(userId: number) {
  const url = `${BASE_URL}/fetchByUser?userId=${encodeURIComponent(userId)}`
  const response = await fetchWithAuth(url)
  return handleResponse<CardResponseDto[]>(response)
}

export async function createCard(payload: CardRequestDto) {
  const response = await fetchWithAuth(BASE_URL, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  })
  return handleResponse<ResponseDto>(response)
}

export async function updateCard(cardId: number, payload: CardRequestDto) {
  const response = await fetchWithAuth(`${BASE_URL}/${cardId}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  })
  return handleResponse<CardResponseDto>(response)
}

export async function deleteCard(cardId: number) {
  const response = await fetchWithAuth(`${BASE_URL}/${cardId}`, { method: 'DELETE' })
  return handleResponse<ResponseDto>(response)
}
