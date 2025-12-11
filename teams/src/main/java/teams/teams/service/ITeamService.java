package teams.teams.service;

import teams.teams.dto.TeamMemberRequestDto;
import teams.teams.dto.TeamMemberResponseDto;

import java.util.List;

public interface ITeamService {

    /**
     * Processes a request from a user to join a team/project
     * 
     * @param cardId the ID of the card/project
     * @param teamMemberRequestDto the request data containing user ID and role
     * @return the created team member
     */
    TeamMemberResponseDto joinTeam(Long cardId, TeamMemberRequestDto teamMemberRequestDto);

    /**
     * Invites a user to join a team/project
     * 
     * @param cardId the ID of the card/project
     * @param teamMemberRequestDto the request data containing user ID and role
     * @return the created team member invitation
     */
    TeamMemberResponseDto inviteToTeam(Long cardId, TeamMemberRequestDto teamMemberRequestDto);

    /**
     * Updates the status of a team member (accept/reject)
     * 
     * @param cardId the ID of the card/project
     * @param userId the ID of the user
     * @param status the new status (JOINED/REJECTED)
     * @return the updated team member
     */
    TeamMemberResponseDto updateMemberStatus(Long cardId, Long userId, String status);

    /**
     * Removes a user from a team/project
     * 
     * @param cardId the ID of the card/project
     * @param userId the ID of the user to remove
     * @return true if the member was removed, false otherwise
     */
    boolean removeMember(Long cardId, Long userId);

    /**
     * Gets all team members for a specific project
     * 
     * @param cardId the ID of the card/project
     * @return list of team members
     */
    List<TeamMemberResponseDto> getTeamMembers(Long cardId);

    /**
     * Gets all team members for a specific project
     *
     * @param cardId the ID of the card/project
     * @return list of team members
     */
    List<TeamMemberResponseDto> getTeamsByMember(Long userId);
}