package teams.teams.mapper;

import teams.teams.api.model.CardRequestDto;
import teams.teams.api.model.CardResponseDto;
import teams.teams.entity.Card;
import teams.teams.entity.Team;

import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CardMapper {

    public static List<CardResponseDto> mapToCardsResponseDto(List<Card> cards) {
        return cards.stream().map(CardMapper::mapToCardResponseDto).collect(Collectors.toList());
    }

    /**
     * Maps Card entity to CardResponseDto
     *
     * @param card Card entity to map
     * @return CardResponseDto with mapped values
     */
    public static CardResponseDto mapToCardResponseDto(Card card) {
        CardResponseDto responseDto = new CardResponseDto();
        responseDto.setId(card.getId());
        responseDto.setTitle(card.getTitle());
        responseDto.setDescription(card.getDescription());
        responseDto.setPosterUrl(URI.create(card.getPosterUrl()));
        responseDto.setOwnerId(card.getOwnerId());
        responseDto.setTeamId(card.getTeam().getId());
        responseDto.setCreatedAt(card.getCreatedAt() != null ? card.getCreatedAt().atZone(ZoneId.systemDefault()).toOffsetDateTime() : null);
        responseDto.setCreatedBy(card.getCreatedBy());
        responseDto.setUpdatedAt(card.getUpdatedAt() != null ? card.getUpdatedAt().atZone(ZoneId.systemDefault()).toOffsetDateTime() : null);
        responseDto.setUpdatedBy(card.getUpdatedBy());
        return responseDto;
    }

    /**
     * Maps CardRequestDto to Card entity
     *
     * @param requestDto CardRequestDto to map
     * @param card       Card entity to update (can be a new instance)
     * @return Card entity with mapped values
     */
    public static Card mapToCard(CardRequestDto requestDto, Card card, Team team) {
        card.setTitle(requestDto.getTitle());
        card.setDescription(requestDto.getDescription());
        card.setPosterUrl(requestDto.getPosterUrl().toString());
        card.setOwnerId(requestDto.getOwnerId());
        card.setTeam(team);
        card.setUpdatedAt(OffsetDateTime.now(ZoneId.systemDefault()).toLocalDateTime());
        return card;
    }
}
