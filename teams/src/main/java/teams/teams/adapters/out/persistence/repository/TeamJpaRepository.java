package teams.teams.adapters.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teams.teams.adapters.out.persistence.entity.TeamJpaEntity;

/**
 * Spring Data JPA repository for TeamJpaEntity.
 * This is in the persistence adapter layer.
 */
@Repository
public interface TeamJpaRepository extends JpaRepository<TeamJpaEntity, Long> {
}
