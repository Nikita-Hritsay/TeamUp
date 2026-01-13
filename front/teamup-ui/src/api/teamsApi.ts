import type { PageResponse, ResponseDto, TeamMemberDto, TeamRequestDto, TeamResponseDto } from '../types'

const BASE_URL = 'http://localhost:8081/api/v1/teams'

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

export async function createTeam(payload: TeamRequestDto) {
  const response = await fetch(`${BASE_URL}/create`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  })
  return handleResponse<ResponseDto>(response)
}

export async function joinTeam(payload: TeamMemberDto) {
  const response = await fetch(`${BASE_URL}/join`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  })
  return handleResponse<ResponseDto>(response)
}

export async function getTeamMembers(cardId: number, params?: { page?: number; size?: number }) {
  const url = new URL(`${BASE_URL}/${cardId}`)
  if (params?.page !== undefined) url.searchParams.set('page', String(params.page))
  if (params?.size !== undefined) url.searchParams.set('size', String(params.size))
  
  const response = await fetch(url.toString())
  return handleResponse<PageResponse<TeamMemberDto>>(response)
}

export async function fetchTeam(teamId: number) {
  const response = await fetch(`${BASE_URL}/fetch?teamId=${encodeURIComponent(teamId)}`)
  return handleResponse<TeamResponseDto>(response)
}
