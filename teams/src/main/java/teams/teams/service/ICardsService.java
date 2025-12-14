package teams.teams.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teams.teams.dto.cards.CardRequestDto;
import teams.teams.dto.cards.CardResponseDto;
import teams.teams.entity.Card;

import java.util.List;

public interface ICardsService {

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
     * Retrieves a card by users ID
     *
     * @param userId the ID of the card to retrieve
     * @return the card with the given ID
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
     * Retrieves all cards
     *
     * @return a page of cards
     */
    List<Card> getAllCards();
}
