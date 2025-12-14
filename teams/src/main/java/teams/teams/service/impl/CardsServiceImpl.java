package teams.teams.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teams.teams.dto.UserDto;
import teams.teams.dto.cards.CardRequestDto;
import teams.teams.dto.cards.CardResponseDto;
import teams.teams.entity.Card;
import teams.teams.mapper.CardMapper;
import teams.teams.repository.CardsRepository;
import teams.teams.service.ICardsService;
import teams.teams.exception.ResourceNotFoundException;

import java.util.List;

@Service
@AllArgsConstructor
public class CardsServiceImpl implements ICardsService {

    private final CardsRepository cardsRepository;

    private final UsersFeignClient usersFeignClient;

    @Override
    public CardResponseDto createCard(CardRequestDto cardRequestDto) {
        ResponseEntity<UserDto> userDtoResponseEntity = usersFeignClient.fetchUser(cardRequestDto.getOwnerId());
        if (userDtoResponseEntity.getStatusCode().is4xxClientError()) {
            throw new ResourceNotFoundException("User", "id", cardRequestDto.getOwnerId().toString());
        }
        Card card = CardMapper.mapToCard(cardRequestDto, new Card());
        Card savedCard = cardsRepository.save(card);
        return CardMapper.mapToCardResponseDto(savedCard);
    }

    @Override
    public CardResponseDto getCardById(Long cardId) {
        Card card = cardsRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "id",
                        cardId.toString()));
        return CardMapper.mapToCardResponseDto(card);
    }

    @Override
    public List<CardResponseDto> getCardsByUserId(Long userId) {
        List<Card> cards = cardsRepository.findByOwnerId(userId);
        return CardMapper.mapToCardsResponseDto(cards);
    }

    @Override
    public CardResponseDto updateCard(Long cardId,
                                      CardRequestDto cardRequestDto) {
        Card existingCard = cardsRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "id",
                        cardId.toString()));

        Card updatedCard = CardMapper.mapToCard(cardRequestDto, existingCard);
        Card savedCard = cardsRepository.save(updatedCard);
        return CardMapper.mapToCardResponseDto(savedCard);
    }

    @Override
    @Transactional
    public boolean deleteCard(Long cardId) {
        Card card = cardsRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "id",
                        cardId.toString()));

        cardsRepository.delete(card);
        return true;
    }

    @Override
    public List<Card> getAllCards() {
        return cardsRepository.findAll();
    }
}
