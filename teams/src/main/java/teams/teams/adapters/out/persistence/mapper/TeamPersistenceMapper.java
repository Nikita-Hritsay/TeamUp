package teams.teams.adapters.out.persistence.mapper;

import teams.teams.domain.model.Team;
import teams.teams.adapters.out.persistence.entity.TeamJpaEntity;

/**
 * Mapper between Team domain model and TeamJpaEntity.
 */
public class TeamPersistenceMapper {

    public static Team toDomain(TeamJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return Team.fromPersistence(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getUpdatedAt(),
                entity.getUpdatedBy()
        );
    }

    public static TeamJpaEntity toEntity(Team domain) {
        if (domain == null) {
            return null;
        }
        TeamJpaEntity entity = new TeamJpaEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setCreatedBy(domain.getCreatedBy());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setUpdatedBy(domain.getUpdatedBy());
        return entity;
    }
}
