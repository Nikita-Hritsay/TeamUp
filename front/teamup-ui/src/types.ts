export type ResponseDto = {
  statusCode: string
  statusMessage: string
}

export type PageResponse<T> = {
  content: T[]
  totalElements: number
  totalPages: number
  number: number
  size: number
}

export type CardRequestDto = {
  title: string
  description?: string
  posterUrl?: string
  ownerId: number
  teamId: number
}

export type CardResponseDto = CardRequestDto & {
  id: number
  createdAt?: string
  createdBy?: string
  updatedAt?: string
  updatedBy?: string
}

export type TeamRequestDto = {
  teamId?: number
  name: string
  description?: string
}

export type TeamMemberDto = {
  id?: number
  cardId?: number
  teamId: number
  userId: number
  role: string
  status?: string
  joinedAt?: string
  createdAt?: string
  createdBy?: string
  updatedAt?: string
  updatedBy?: string
}

export type RoleDto = {
  roleName: string
}

export type UserDto = {
  Id?: number
  firstName: string
  lastName: string
  email: string
  mobileNumber: string
  roles?: RoleDto[]
  teamMember?: TeamMemberDto
  cards?: CardResponseDto[]
}
