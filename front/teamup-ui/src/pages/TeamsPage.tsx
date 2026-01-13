import { useEffect, useMemo, useState } from 'react'
import type { FormEvent } from 'react'
import { createTeam, getTeamMembers } from '../api/teamsApi'
import type { TeamMemberDto, TeamRequestDto, PageResponse } from '../types'

export function TeamsPage() {
  const [teamForm, setTeamForm] = useState<TeamRequestDto>({
    name: '',
    description: '',
  })
  const [teamMessage, setTeamMessage] = useState('')
  const [teamError, setTeamError] = useState('')

  const [membersCardId, setMembersCardId] = useState('')
  const [membersPage, setMembersPage] = useState<PageResponse<TeamMemberDto> | null>(null)
  const [membersError, setMembersError] = useState('')
  const [membersLoading, setMembersLoading] = useState(false)
  const [membersPageNum, setMembersPageNum] = useState(0)
  const [membersPageSize, setMembersPageSize] = useState(10)

  const handleCreateTeam = async (event: FormEvent) => {
    event.preventDefault()
    setTeamMessage('')
    setTeamError('')
    if (!teamForm.name) {
      setTeamError('Team name is required.')
      return
    }
    try {
      await createTeam(teamForm)
      setTeamMessage('Team created successfully.')
      setTeamForm({ name: '', description: '' })
    } catch (err) {
      setTeamError(err instanceof Error ? err.message : 'Unable to create team.')
    }
  }

  const handleFetchMembers = async (event?: FormEvent) => {
    if (event) event.preventDefault()
    const parsedCardId = Number(membersCardId)
    if (!parsedCardId) {
      setMembersError('Please provide a valid Card ID.')
      setMembersPage(null)
      return
    }
    setMembersError('')
    setMembersLoading(true)
    try {
      const data = await getTeamMembers(parsedCardId, {
        page: membersPageNum,
        size: membersPageSize,
      })
      setMembersPage(data)
    } catch (err) {
      setMembersError(err instanceof Error ? err.message : 'Unable to load members.')
      setMembersPage(null)
    } finally {
      setMembersLoading(false)
    }
  }

  useEffect(() => {
    if (membersCardId) {
      handleFetchMembers()
    }
  }, [membersPageNum, membersPageSize])

  const totalMembersPages = useMemo(() => membersPage?.totalPages ?? 0, [membersPage])

  return (
    <section>
      <h2>Teams</h2>
      <div className="panel">
        <h3>Create Team</h3>
        <form className="form" onSubmit={handleCreateTeam}>
          <label className="form-field">
            <span>Name</span>
            <input
              type="text"
              value={teamForm.name}
              onChange={(event) =>
                setTeamForm((prev) => ({ ...prev, name: event.target.value }))
              }
              required
            />
          </label>
          <label className="form-field">
            <span>Description</span>
            <textarea
              value={teamForm.description}
              onChange={(event) =>
                setTeamForm((prev) => ({ ...prev, description: event.target.value }))
              }
            />
          </label>
          <button type="submit">Create</button>
        </form>
        {teamError && <div className="error">{teamError}</div>}
        {teamMessage && <div className="success">{teamMessage}</div>}
      </div>

      <div className="panel">
        <h3>Team Members by Card</h3>
        <form className="form inline-form" onSubmit={handleFetchMembers}>
          <label className="form-field">
            <span>Card ID</span>
            <input
              type="number"
              min={1}
              value={membersCardId}
              onChange={(event) => {
                setMembersCardId(event.target.value)
                setMembersPageNum(0)
              }}
              required
            />
          </label>
          <label className="form-field small">
            <span>Page Size</span>
            <input
              type="number"
              min={1}
              max={50}
              value={membersPageSize}
              onChange={(event) => {
                const nextSize = Number(event.target.value)
                setMembersPageSize(Number.isNaN(nextSize) ? 10 : nextSize)
                setMembersPageNum(0)
              }}
            />
          </label>
          <button type="submit" disabled={membersLoading}>
            {membersLoading ? 'Loading…' : 'Load Members'}
          </button>
        </form>
        {membersError && <div className="error">{membersError}</div>}
        {membersLoading && <p>Loading members…</p>}
        {!membersLoading && membersPage && (
          <>
            {membersPage.content.length === 0 ? (
              <p className="muted">No members to display.</p>
            ) : (
              <>
                <ul className="list">
                  {membersPage.content.map((member) => (
                    <li
                      key={member.id ?? `${member.userId}-${member.teamId}`}
                      className="list-item"
                    >
                      <div className="list-title">
                        <strong>User {member.userId}</strong>
                        <span className="muted">Team {member.teamId}</span>
                      </div>
                      <p className="muted">
                        Role: {member.role} {member.status ? `• Status: ${member.status}` : ''}
                      </p>
                      {member.cardId && <p className="muted">Card: {member.cardId}</p>}
                    </li>
                  ))}
                </ul>
                {totalMembersPages > 0 && (
                  <div className="pagination">
                    <button
                      type="button"
                      onClick={() => setMembersPageNum((prev) => Math.max(prev - 1, 0))}
                      disabled={membersPageNum === 0}
                    >
                      Previous
                    </button>
                    <span>
                      Page {membersPageNum + 1} of {totalMembersPages}
                    </span>
                    <button
                      type="button"
                      onClick={() =>
                        setMembersPageNum((prev) =>
                          membersPage.totalPages
                            ? Math.min(prev + 1, totalMembersPages - 1)
                            : prev,
                        )
                      }
                      disabled={membersPageNum + 1 >= totalMembersPages}
                    >
                      Next
                    </button>
                  </div>
                )}
              </>
            )}
          </>
        )}
        {!membersLoading && !membersPage && !membersCardId && (
          <p className="muted">Enter a Card ID to load team members.</p>
        )}
      </div>
    </section>
  )
}

