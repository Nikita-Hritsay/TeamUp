package cards.cards.repository;

import cards.cards.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CardsRepository extends JpaRepository<Card, Long>, JpaSpecificationExecutor<Card> {
    // JpaRepository provides basic CRUD operations and pagination support
    // JpaSpecificationExecutor provides support for filtering using Specifications
}
