import type { ResponseDto, UserDto } from '../types'
import { fetchWithAuth, handleResponse } from './client'

const BASE_URL = import.meta.env.VITE_USERS_API_URL ?? 'http://localhost:8080/api/v1'

export async function fetchUser(id: number) {
  const response = await fetchWithAuth(`${BASE_URL}/fetch?id=${encodeURIComponent(id)}`)
  return handleResponse<UserDto>(response)
}

export async function createUser(payload: UserDto) {
  const response = await fetchWithAuth(`${BASE_URL}/create`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  })
  return handleResponse<ResponseDto>(response)
}

export async function updateUser(payload: UserDto) {
  const response = await fetchWithAuth(`${BASE_URL}/update`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  })
  return handleResponse<ResponseDto>(response)
}
