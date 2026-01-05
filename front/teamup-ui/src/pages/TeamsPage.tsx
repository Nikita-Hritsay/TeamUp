import { useState } from 'react'
import type { FormEvent } from 'react'
import { joinTeam, createTeam, getTeamMembers } from '../api/teamsApi'
import type { TeamMemberDto, TeamRequestDto } from '../types'

export function TeamsPage() {
  const [teamForm, setTeamForm] = useState<TeamRequestDto>({
    name: '',
    description: '',
  })
  const [teamMessage, setTeamMessage] = useState('')
  const [teamError, setTeamError] = useState('')

  const [joinForm, setJoinForm] = useState<TeamMemberDto>({
    userId: 0,
    role: 'PARTICIPANT',
    teamId: 0,
  })
  const [joinMessage, setJoinMessage] = useState('')
  const [joinError, setJoinError] = useState('')

  const [membersCardId, setMembersCardId] = useState('')
  const [members, setMembers] = useState<TeamMemberDto[]>([])
  const [membersError, setMembersError] = useState('')

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

  const handleJoinTeam = async (event: FormEvent) => {
    event.preventDefault()
    setJoinMessage('')
    setJoinError('')
    if (!joinForm.userId || !joinForm.teamId || !joinForm.role) {
      setJoinError('User ID, Team ID, and Role are required.')
      return
    }
    try {
      await joinTeam(joinForm)
      setJoinMessage('Join request submitted.')
    } catch (err) {
      setJoinError(err instanceof Error ? err.message : 'Unable to join team.')
    }
  }

  const handleFetchMembers = async (event: FormEvent) => {
    event.preventDefault()
    const parsedCardId = Number(membersCardId)
    if (!parsedCardId) {
      setMembersError('Please provide a valid Card ID.')
      setMembers([])
      return
    }
    setMembersError('')
    try {
      const data = await getTeamMembers(parsedCardId)
      setMembers(data)
    } catch (err) {
      setMembersError(err instanceof Error ? err.message : 'Unable to load members.')
      setMembers([])
    }
  }

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
        <h3>Join Team</h3>
        <form className="form" onSubmit={handleJoinTeam}>
          <label className="form-field">
            <span>User ID</span>
            <input
              type="number"
              min={1}
              value={joinForm.userId || ''}
              onChange={(event) =>
                setJoinForm((prev) => ({ ...prev, userId: Number(event.target.value) }))
              }
              required
            />
          </label>
          <label className="form-field">
            <span>Team ID</span>
            <input
              type="number"
              min={1}
              value={joinForm.teamId || ''}
              onChange={(event) =>
                setJoinForm((prev) => ({ ...prev, teamId: Number(event.target.value) }))
              }
              required
            />
          </label>
          <label className="form-field">
            <span>Card ID</span>
            <input
              type="number"
              min={1}
              value={joinForm.cardId ?? ''}
              onChange={(event) =>
                setJoinForm((prev) => ({
                  ...prev,
                  cardId: event.target.value ? Number(event.target.value) : undefined,
                }))
              }
            />
          </label>
          <label className="form-field">
            <span>Role</span>
            <select
              value={joinForm.role}
              onChange={(event) =>
                setJoinForm((prev) => ({ ...prev, role: event.target.value }))
              }
            >
              <option value="CREATOR">CREATOR</option>
              <option value="PARTICIPANT">PARTICIPANT</option>
              <option value="LEADER">LEADER</option>
            </select>
          </label>
          <button type="submit">Join</button>
        </form>
        {joinError && <div className="error">{joinError}</div>}
        {joinMessage && <div className="success">{joinMessage}</div>}
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
              onChange={(event) => setMembersCardId(event.target.value)}
              required
            />
          </label>
          <button type="submit">Load Members</button>
        </form>
        {membersError && <div className="error">{membersError}</div>}
        {!membersError && members.length === 0 && (
          <p className="muted">No members to display.</p>
        )}
        {members.length > 0 && (
          <ul className="list">
            {members.map((member) => (
              <li key={member.id ?? `${member.userId}-${member.teamId}`} className="list-item">
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
        )}
      </div>
    </section>
  )
}
