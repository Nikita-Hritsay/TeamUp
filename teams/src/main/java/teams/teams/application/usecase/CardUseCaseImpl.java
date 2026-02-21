package teams.teams.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teams.teams.api.model.CardRequestDto;
import teams.teams.api.model.CardResponseDto;
import teams.teams.application.mapper.CardDomainMapper;
import teams.teams.application.port.in.CardUseCase;
import teams.teams.application.port.out.CardRepositoryPort;
import teams.teams.application.port.out.TeamRepositoryPort;
import teams.teams.application.port.out.UserClientPort;
import teams.teams.domain.model.Card;
import teams.teams.exception.ResourceNotFoundException;

import java.util.List;

/**
 * Implementation of CardUseCase - orchestrates card-related business operations.
 */
@Service
@RequiredArgsConstructor
public class CardUseCaseImpl implements CardUseCase {

    private final CardRepositoryPort cardRepository;
    private final UserClientPort userClient;
    private final TeamRepositoryPort teamRepository;

    @Override
    @Transactional
    public CardResponseDto createCard(CardRequestDto cardRequestDto) {
        // Validate user exists
        userClient.fetchUser(cardRequestDto.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", cardRequestDto.getOwnerId().toString()));
        
        // Validate team exists
        teamRepository.findById(cardRequestDto.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", cardRequestDto.getTeamId().toString()));
        
        Card card = CardDomainMapper.toDomain(cardRequestDto);
        Card savedCard = cardRepository.save(card);
        return CardDomainMapper.toDto(savedCard);
    }

    @Override
    public CardResponseDto getCardById(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "id", cardId.toString()));
        return CardDomainMapper.toDto(card);
    }

    @Override
    public List<CardResponseDto> getCardsByUserId(Long userId) {
        List<Card> cards = cardRepository.findByOwnerId(userId);
        return CardDomainMapper.toDtoList(cards);
    }

    @Override
    @Transactional
    public CardResponseDto updateCard(Long cardId, CardRequestDto cardRequestDto) {
        Card existingCard = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "id", cardId.toString()));

        teamRepository.findById(cardRequestDto.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", cardRequestDto.getTeamId().toString()));

        existingCard.update(
                cardRequestDto.getTitle(),
                cardRequestDto.getDescription(),
                cardRequestDto.getPosterUrl(),
                cardRequestDto.getTeamId()
        );
        
        Card savedCard = cardRepository.save(existingCard);
        return CardDomainMapper.toDto(savedCard);
    }

    @Override
    @Transactional
    public boolean deleteCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "id", cardId.toString()));

        cardRepository.delete(card);
        return true;
    }

    @Override
    public Page<CardResponseDto> getAllCards(Pageable pageable) {
        Page<Card> cardsPage = cardRepository.findAll(pageable);
        return cardsPage.map(CardDomainMapper::toDto);
    }

    @Override
    public Page<CardResponseDto> getFilteredCards(Long ownerId, String title, Pageable pageable) {
        Page<Card> filteredCards = cardRepository.findByFilters(ownerId, title, pageable);
        return filteredCards.map(CardDomainMapper::toDto);
    }
}
