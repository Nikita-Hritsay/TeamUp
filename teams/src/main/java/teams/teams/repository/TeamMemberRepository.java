package teams.teams.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import teams.teams.entity.TeamMember;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long>, JpaSpecificationExecutor<TeamMember> {

    /**
     * Find all team members for a specific team with pagination.
     */
    Page<TeamMember> findByTeamId(Long teamId, Pageable pageable);

    /**
     * Find all team members for a specific card
     * 
     * @param cardId the ID of the card
     * @return list of team members
     */
    List<TeamMember> findByCardId(Long cardId);
    
    /**
     * Find a team member by card ID and user ID
     * 
     * @param cardId the ID of the card
     * @param userId the ID of the user
     * @return optional containing the team member if found
     */
    Optional<TeamMember> findByCardIdAndUserId(Long cardId, Long userId);

    /**
     * Find a team member by team Id and user ID
     *
     * @param teamId the ID of the card
     * @param userId the ID of the user
     * @return optional containing the team member if found
     */
    Optional<TeamMember> findByTeamIdAndUserId(Long teamId, Long userId);
}