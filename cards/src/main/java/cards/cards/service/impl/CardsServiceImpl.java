package cards.cards.service.impl;

import cards.cards.dto.CardRequestDto;
import cards.cards.dto.CardResponseDto;
import cards.cards.entity.Card;
import cards.cards.exception.ResourceNotFoundException;
import cards.cards.mapper.CardMapper;
import cards.cards.repository.CardsRepository;
import cards.cards.service.ICardsService;
import cards.cards.specification.CardSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CardsServiceImpl implements ICardsService
{

    private final CardsRepository cardsRepository;

    @Override
    public CardResponseDto createCard(CardRequestDto cardRequestDto)
    {
        Card card = CardMapper.mapToCard(cardRequestDto, new Card());
        Card savedCard = cardsRepository.save(card);
        return CardMapper.mapToCardResponseDto(savedCard);
    }

    @Override
    public CardResponseDto getCardById(Long cardId)
    {
        Card card = cardsRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "id",
                        cardId.toString()));
        return CardMapper.mapToCardResponseDto(card);
    }

    @Override
    public CardResponseDto updateCard(Long cardId,
            CardRequestDto cardRequestDto)
    {
        Card existingCard = cardsRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "id",
                        cardId.toString()));

        Card updatedCard = CardMapper.mapToCard(cardRequestDto, existingCard);
        Card savedCard = cardsRepository.save(updatedCard);
        return CardMapper.mapToCardResponseDto(savedCard);
    }

    @Override
    @Transactional
    public boolean deleteCard(Long cardId)
    {
        Card card = cardsRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "id",
                        cardId.toString()));

        cardsRepository.delete(card);
        return true;
    }

    @Override
    public Page<CardResponseDto> getAllCards(Pageable pageable)
    {
        Page<Card> cardsPage = cardsRepository.findAll(pageable);
        return cardsPage.map(CardMapper::mapToCardResponseDto);
    }

    @Override
    public Page<CardResponseDto> getFilteredCards(Long ownerId, String title,
            Pageable pageable)
    {
        Specification<Card> spec = CardSpecification.where(
                CardSpecification.hasOwnerId(ownerId),
                CardSpecification.titleContains(title)
        );

        Page<Card> filteredCards = cardsRepository.findAll(spec, pageable);
        return filteredCards.map(CardMapper::mapToCardResponseDto);
    }
}
