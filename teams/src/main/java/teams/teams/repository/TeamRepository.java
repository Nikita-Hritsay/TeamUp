package teams.teams.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import teams.teams.entity.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    /**
     * Find teams where the given user is a member (for "my teams").
     */
    @Query("SELECT t FROM team t WHERE t.id IN (SELECT tm.teamId FROM team_member tm WHERE tm.userId = :userId)")
    Page<Team> findTeamsByMemberUserId(@Param("userId") Long userId, Pageable pageable);
}
