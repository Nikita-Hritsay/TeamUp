package teams.teams.adapters.out.persistence.mapper;

import teams.teams.domain.model.Card;
import teams.teams.adapters.out.persistence.entity.CardJpaEntity;

import java.net.URI;

/**
 * Mapper between Card domain model and CardJpaEntity.
 */
public class CardPersistenceMapper {

    public static Card toDomain(CardJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        URI posterUrl = entity.getPosterUrl() != null ? URI.create(entity.getPosterUrl()) : null;
        return Card.fromPersistence(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                posterUrl,
                entity.getTeam().getId(),
                entity.getOwnerId(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getUpdatedAt(),
                entity.getUpdatedBy()
        );
    }

    public static CardJpaEntity toEntity(Card domain, TeamJpaEntity teamEntity) {
        if (domain == null) {
            return null;
        }
        CardJpaEntity entity = new CardJpaEntity();
        entity.setId(domain.getId());
        entity.setTitle(domain.getTitle());
        entity.setDescription(domain.getDescription());
        entity.setPosterUrl(domain.getPosterUrl() != null ? domain.getPosterUrl().toString() : null);
        entity.setTeam(teamEntity);
        entity.setOwnerId(domain.getOwnerId());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setCreatedBy(domain.getCreatedBy());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setUpdatedBy(domain.getUpdatedBy());
        return entity;
    }
}
