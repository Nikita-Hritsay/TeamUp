package teams.teams.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import teams.teams.entity.Card;

import java.util.List;

@Repository
public interface CardsRepository extends JpaRepository<Card, Long>, JpaSpecificationExecutor<Card> {
    List<Card> findByOwnerId(Long ownerId);

    List<Card> findByTeam_Id(Long teamId);
}
