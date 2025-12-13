package teams.teams.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teams.teams.entity.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
}
