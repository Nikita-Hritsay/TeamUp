import type { PageResponse, ResponseDto, TeamMemberDto, TeamMemberRequestDto, TeamRequestDto, TeamResponseDto } from '../types'
import { fetchWithAuth, handleResponse } from './client'

const BASE_URL = import.meta.env.VITE_TEAMS_API_URL ?? 'http://localhost:8081/api/v1/teams'

const DEFAULT_PAGE_SIZE = 100

export async function listTeams(params?: { page?: number; size?: number; userId?: number }) {
  const url = new URL(BASE_URL)
  if (params?.page !== undefined) url.searchParams.set('page', String(params.page))
  if (params?.size !== undefined) url.searchParams.set('size', String(params.size))
  if (params?.userId !== undefined) url.searchParams.set('userId', String(params.userId))
  const response = await fetchWithAuth(url.toString())
  return handleResponse<PageResponse<TeamResponseDto>>(response)
}

/** Fetch all teams (no pagination UI). Uses multiple pages if needed. */
export async function listAllTeams(): Promise<TeamResponseDto[]> {
  const all: TeamResponseDto[] = []
  let page = 0
  let hasMore = true
  while (hasMore) {
    const res = await listTeams({ page, size: DEFAULT_PAGE_SIZE })
    all.push(...res.content)
    hasMore = res.content.length === DEFAULT_PAGE_SIZE && page + 1 < res.totalPages
    page += 1
  }
  return all
}

export async function getTeamMembersByTeamId(
  teamId: number,
  params?: { page?: number; size?: number }
) {
  const url = new URL(`${BASE_URL}/${teamId}/members`)
  if (params?.page !== undefined) url.searchParams.set('page', String(params.page))
  if (params?.size !== undefined) url.searchParams.set('size', String(params.size))
  const response = await fetchWithAuth(url.toString())
  return handleResponse<PageResponse<TeamMemberDto>>(response)
}

export async function createTeam(payload: TeamRequestDto) {
  const response = await fetchWithAuth(`${BASE_URL}/create`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  })
  return handleResponse<ResponseDto>(response)
}

export async function joinTeam(payload: TeamMemberRequestDto) {
  const response = await fetchWithAuth(`${BASE_URL}/join`, {
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

  const response = await fetchWithAuth(url.toString())
  return handleResponse<PageResponse<TeamMemberDto>>(response)
}

export async function fetchTeam(teamId: number) {
  const response = await fetchWithAuth(`${BASE_URL}/fetch?teamId=${encodeURIComponent(teamId)}`)
  return handleResponse<TeamResponseDto>(response)
}
