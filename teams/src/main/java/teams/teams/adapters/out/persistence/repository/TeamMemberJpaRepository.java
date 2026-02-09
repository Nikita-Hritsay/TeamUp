package teams.teams.adapters.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import teams.teams.adapters.out.persistence.entity.TeamMemberJpaEntity;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for TeamMemberJpaEntity.
 * This is in the persistence adapter layer.
 */
@Repository
public interface TeamMemberJpaRepository extends JpaRepository<TeamMemberJpaEntity, Long>, JpaSpecificationExecutor<TeamMemberJpaEntity> {
    
    /**
     * Find all team members for a specific card
     * 
     * @param cardId the ID of the card
     * @return list of team members
     */
    List<TeamMemberJpaEntity> findByCardId(Long cardId);
    
    /**
     * Find a team member by card ID and user ID
     * 
     * @param cardId the ID of the card
     * @param userId the ID of the user
     * @return optional containing the team member if found
     */
    Optional<TeamMemberJpaEntity> findByCardIdAndUserId(Long cardId, Long userId);

    /**
     * Find a team member by team Id and user ID
     *
     * @param teamId the ID of the team
     * @param userId the ID of the user
     * @return optional containing the team member if found
     */
    Optional<TeamMemberJpaEntity> findByTeamIdAndUserId(Long teamId, Long userId);
}
