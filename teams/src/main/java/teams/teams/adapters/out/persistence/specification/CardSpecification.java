package teams.teams.adapters.out.persistence.specification;

import org.springframework.data.jpa.domain.Specification;
import teams.teams.adapters.out.persistence.entity.CardJpaEntity;

/**
 * Specification class for CardJpaEntity to support filtering.
 * This is in the persistence adapter layer as it's JPA-specific.
 */
public class CardSpecification {

    /**
     * Creates a specification to filter cards by owner ID
     *
     * @param ownerId the owner ID to filter by
     * @return a specification that filters cards by owner ID
     */
    public static Specification<CardJpaEntity> hasOwnerId(Long ownerId) {
        return (root, query, criteriaBuilder) -> {
            if (ownerId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("ownerId"), ownerId);
        };
    }

    /**
     * Creates a specification to filter cards by title (case-insensitive, contains)
     *
     * @param title the title to filter by
     * @return a specification that filters cards by title
     */
    public static Specification<CardJpaEntity> titleContains(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null || title.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")),
                    "%" + title.toLowerCase() + "%"
            );
        };
    }

    /**
     * Combines multiple specifications with AND operator
     *
     * @param specs the specifications to combine
     * @return a combined specification
     */
    @SafeVarargs
    public static Specification<CardJpaEntity> where(Specification<CardJpaEntity>... specs) {
        Specification<CardJpaEntity> result = Specification.where(null);
        for (Specification<CardJpaEntity> spec : specs) {
            result = result.and(spec);
        }
        return result;
    }
}
