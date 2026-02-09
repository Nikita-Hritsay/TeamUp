package teams.teams.application.port.out;

import teams.teams.domain.model.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Outbound port for Card persistence operations.
 * This interface defines what the application needs from the persistence layer.
 */
public interface CardRepositoryPort {

    /**
     * Saves a card
     *
     * @param card the card to save
     * @return the saved card
     */
    Card save(Card card);

    /**
     * Finds a card by ID
     *
     * @param id the card ID
     * @return optional containing the card if found
     */
    Optional<Card> findById(Long id);

    /**
     * Finds cards by owner ID
     *
     * @param ownerId the owner ID
     * @return list of cards
     */
    List<Card> findByOwnerId(Long ownerId);

    /**
     * Finds all cards with pagination
     *
     * @param pageable pagination information
     * @return a page of cards
     */
    Page<Card> findAll(Pageable pageable);

    /**
     * Finds cards filtered by ownerId and/or title with pagination
     *
     * @param ownerId the owner ID to filter by (optional)
     * @param title the title to filter by (optional)
     * @param pageable pagination information
     * @return a page of filtered cards
     */
    Page<Card> findByFilters(Long ownerId, String title, Pageable pageable);

    /**
     * Deletes a card
     *
     * @param card the card to delete
     */
    void delete(Card card);

    /**
     * Checks if a card exists by ID
     *
     * @param id the card ID
     * @return true if exists, false otherwise
     */
    boolean existsById(Long id);
}
