package teams.teams.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teams.teams.api.model.CardRequestDto;
import teams.teams.api.model.CardResponseDto;
import teams.teams.dto.UserDto;
import teams.teams.entity.TeamMember;
import teams.teams.mapper.CardMapper;
import teams.teams.repository.CardsRepository;
import teams.teams.service.ICardsService;
import teams.teams.service.client.UsersFeignClient;
import teams.teams.specification.CardSpecification;
import teams.teams.entity.Card;
import teams.teams.entity.Team;
import teams.teams.exception.ResourceNotFoundException;
import teams.teams.repository.TeamRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class CardsServiceImpl implements ICardsService {

    private final CardsRepository cardsRepository;

    private final UsersFeignClient usersFeignClient;
    private final TeamRepository teamRepository;

    @Override
    public CardResponseDto createCard(CardRequestDto cardRequestDto) {
        ResponseEntity<UserDto> userDtoResponseEntity = usersFeignClient.fetchUser(cardRequestDto.getOwnerId());
        if (userDtoResponseEntity.getStatusCode().is4xxClientError()) {
            throw new ResourceNotFoundException("User", "id", cardRequestDto.getOwnerId().toString());
        }
        Team team = teamRepository.findById(cardRequestDto.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", cardRequestDto.getTeamId().toString()));
        // add logic whether user is member of team or not
        Card card = CardMapper.mapToCard(cardRequestDto, new Card(), team);
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
        ResponseEntity<UserDto> userDtoResponseEntity = usersFeignClient.fetchUser(userId);
        if (userDtoResponseEntity.getStatusCode().is4xxClientError()) {
            throw new ResourceNotFoundException("User", "id", userId.toString());
        }
        List<Card> cards = cardsRepository.findByOwnerId(userId);
        return CardMapper.mapToCardsResponseDto(cards);
    }

    @Override
    public List<CardResponseDto> getCardsByTeamId(Long teamId) {
        teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", teamId.toString()));
        List<Card> cards = cardsRepository.findByTeam_Id(teamId);
        return CardMapper.mapToCardsResponseDto(cards);
    }

    @Override
    public CardResponseDto updateCard(Long cardId,
                                      CardRequestDto cardRequestDto) {
        Card existingCard = cardsRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "id",
                        cardId.toString()));

        Team team = teamRepository.findById(cardRequestDto.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", cardRequestDto.getTeamId().toString()));
        Card updatedCard = CardMapper.mapToCard(cardRequestDto, existingCard, team);
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
    public Page<CardResponseDto> getAllCards(Pageable pageable) {
        Page<Card> cardsPage = cardsRepository.findAll(pageable);
        return cardsPage.map(CardMapper::mapToCardResponseDto);
    }

    @Override
    public Page<CardResponseDto> getFilteredCards(Long ownerId, String title, Long teamId,
                                                  Pageable pageable) {
        Specification<Card> spec = CardSpecification.where(
                CardSpecification.hasOwnerId(ownerId),
                CardSpecification.titleContains(title),
                CardSpecification.hasTeamId(teamId)
        );

        Page<Card> filteredCards = cardsRepository.findAll(spec, pageable);
        return filteredCards.map(CardMapper::mapToCardResponseDto);
    }
}
