package teams.teams.adapters.out.persistence.mapper;

import teams.teams.domain.model.TeamMember;
import teams.teams.adapters.out.persistence.entity.TeamMemberJpaEntity;

/**
 * Mapper between TeamMember domain model and TeamMemberJpaEntity.
 */
public class TeamMemberPersistenceMapper {

    public static TeamMember toDomain(TeamMemberJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return TeamMember.fromPersistence(
                entity.getId(),
                entity.getTeamId(),
                entity.getCardId(),
                entity.getUserId(),
                entity.getRole(),
                entity.getStatus(),
                entity.getJoinedAt(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getUpdatedAt(),
                entity.getUpdatedBy()
        );
    }

    public static TeamMemberJpaEntity toEntity(TeamMember domain) {
        if (domain == null) {
            return null;
        }
        TeamMemberJpaEntity entity = new TeamMemberJpaEntity();
        entity.setId(domain.getId());
        entity.setTeamId(domain.getTeamId());
        entity.setCardId(domain.getCardId());
        entity.setUserId(domain.getUserId());
        entity.setRole(domain.getRole());
        entity.setStatus(domain.getStatusAsString());
        entity.setJoinedAt(domain.getJoinedAt());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setCreatedBy(domain.getCreatedBy());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setUpdatedBy(domain.getUpdatedBy());
        return entity;
    }
}
