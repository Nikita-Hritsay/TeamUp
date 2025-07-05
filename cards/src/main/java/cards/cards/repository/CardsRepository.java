package cards.cards.repository;

import cards.cards.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.lang.ScopedValue;
import java.util.List;

@Repository
public interface CardsRepository extends JpaRepository<Card, Long>, JpaSpecificationExecutor<Card> {
    List<Card> findByOwnerId(Long ownerId);
    // JpaRepository provides basic CRUD operations and pagination support
    // JpaSpecificationExecutor provides support for filtering using Specifications
}
