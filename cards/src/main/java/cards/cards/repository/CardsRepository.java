package cards.cards.repository;

import cards.cards.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardsRepository extends JpaRepository<Card, Long>, JpaSpecificationExecutor<Card> {
    List<Card> findByOwnerId(Long ownerId);
}
