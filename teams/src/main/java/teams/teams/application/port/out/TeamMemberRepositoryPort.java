package teams.teams.application.port.out;

import teams.teams.domain.model.TeamMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Outbound port for TeamMember persistence operations.
 * This interface defines what the application needs from the persistence layer.
 */
public interface TeamMemberRepositoryPort {

    /**
     * Saves a team member
     *
     * @param teamMember the team member to save
     * @return the saved team member
     */
    TeamMember save(TeamMember teamMember);

    /**
     * Finds a team member by card ID and user ID
     *
     * @param cardId the card ID
     * @param userId the user ID
     * @return optional containing the team member if found
     */
    Optional<TeamMember> findByCardIdAndUserId(Long cardId, Long userId);

    /**
     * Finds a team member by team ID and user ID
     *
     * @param teamId the team ID
     * @param userId the user ID
     * @return optional containing the team member if found
     */
    Optional<TeamMember> findByTeamIdAndUserId(Long teamId, Long userId);

    /**
     * Finds team members by card ID with pagination
     *
     * @param cardId the card ID
     * @param pageable pagination information
     * @return a page of team members
     */
    Page<TeamMember> findByCardId(Long cardId, Pageable pageable);

    /**
     * Deletes a team member
     *
     * @param teamMember the team member to delete
     */
    void delete(TeamMember teamMember);
}
