package teams.teams.adapters.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import teams.teams.adapters.out.persistence.entity.CardJpaEntity;

import java.util.List;

/**
 * Spring Data JPA repository for CardJpaEntity.
 * This is in the persistence adapter layer.
 */
@Repository
public interface CardJpaRepository extends JpaRepository<CardJpaEntity, Long>, JpaSpecificationExecutor<CardJpaEntity> {
    List<CardJpaEntity> findByOwnerId(Long ownerId);
}
