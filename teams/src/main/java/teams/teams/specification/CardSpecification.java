package teams.teams.specification;

import org.springframework.data.jpa.domain.Specification;
import teams.teams.entity.Card;

/**
 * Specification class for Card entity to support filtering
 */
public class CardSpecification {

    /**
     * Creates a specification to filter cards by owner ID
     *
     * @param ownerId the owner ID to filter by
     * @return a specification that filters cards by owner ID
     */
    public static Specification<Card> hasOwnerId(Long ownerId) {
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
    public static Specification<Card> titleContains(String title) {
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
    public static Specification<Card> where(Specification<Card>... specs) {
        Specification<Card> result = Specification.where(null);
        for (Specification<Card> spec : specs) {
            result = result.and(spec);
        }
        return result;
    }
}
