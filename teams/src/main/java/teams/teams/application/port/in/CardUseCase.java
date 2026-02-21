package teams.teams.application.port.in;

import teams.teams.api.model.CardRequestDto;
import teams.teams.api.model.CardResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Inbound port (use case interface) for Card operations.
 * This interface defines the application's use cases from the perspective of the outside world.
 */
public interface CardUseCase {

    /**
     * Creates a new card
     *
     * @param cardRequestDto the card data to create
     * @return the created card
     */
    CardResponseDto createCard(CardRequestDto cardRequestDto);

    /**
     * Retrieves a card by its ID
     *
     * @param cardId the ID of the card to retrieve
     * @return the card with the given ID
     */
    CardResponseDto getCardById(Long cardId);

    /**
     * Retrieves cards by user ID
     *
     * @param userId the ID of the user
     * @return list of cards for the user
     */
    List<CardResponseDto> getCardsByUserId(Long userId);

    /**
     * Updates an existing card
     *
     * @param cardId the ID of the card to update
     * @param cardRequestDto the new card data
     * @return the updated card
     */
    CardResponseDto updateCard(Long cardId, CardRequestDto cardRequestDto);

    /**
     * Deletes a card by its ID
     *
     * @param cardId the ID of the card to delete
     * @return true if the card was deleted, false otherwise
     */
    boolean deleteCard(Long cardId);

    /**
     * Retrieves all cards with pagination
     *
     * @param pageable pagination information
     * @return a page of cards
     */
    Page<CardResponseDto> getAllCards(Pageable pageable);

    /**
     * Retrieves cards filtered by ownerId and/or title with pagination
     *
     * @param ownerId the owner ID to filter by (optional)
     * @param title the title to filter by (optional)
     * @param pageable pagination information
     * @return a page of filtered cards
     */
    Page<CardResponseDto> getFilteredCards(Long ownerId, String title, Pageable pageable);
}
