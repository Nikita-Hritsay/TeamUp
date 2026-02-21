package teams.teams.application.port.in;

import teams.teams.api.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Inbound port (use case interface) for Team operations.
 * This interface defines the application's use cases from the perspective of the outside world.
 */
public interface TeamUseCase {

    /**
     * Processes a request from a user to join a team/project
     * 
     * @param teamMemberRequestDto the request data containing user ID and role
     * @return the created team member
     */
    TeamMemberResponseDto joinTeam(TeamMemberRequestDto teamMemberRequestDto);

    /**
     * Processes a request from a user to create a team
     *
     * @param teamRequestDto the request data containing team details
     * @return the created team
     */
    TeamResponseDto createTeam(TeamRequestDto teamRequestDto);

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
     * Gets all team members for a specific project with pagination
     * 
     * @param cardId the ID of the card/project
     * @param pageable pagination information
     * @return a page of team members
     */
    Page<TeamMemberResponseDto> getTeamMembers(Long cardId, Pageable pageable);

    /**
     * Gets all teams for a specific member
     *
     * @param userId the ID of the user
     * @return list of team members
     */
    List<TeamMemberResponseDto> getTeamsByMember(Long userId);

    /**
     * Fetches a team by ID
     *
     * @param teamId the ID of the team
     * @return the team response DTO
     */
    TeamResponseDto fetchTeam(Long teamId);
}
