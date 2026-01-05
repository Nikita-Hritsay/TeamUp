import type { ResponseDto, UserDto } from '../types'

const BASE_URL = 'http://localhost:8080/api/v1'

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

export async function fetchUser(id: number) {
  const response = await fetch(`${BASE_URL}/fetch?id=${encodeURIComponent(id)}`)
  return handleResponse<UserDto>(response)
}

export async function createUser(payload: UserDto) {
  const response = await fetch(`${BASE_URL}/create`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  })
  return handleResponse<ResponseDto>(response)
}

export async function updateUser(payload: UserDto) {
  const response = await fetch(`${BASE_URL}/update`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  })
  return handleResponse<ResponseDto>(response)
}
