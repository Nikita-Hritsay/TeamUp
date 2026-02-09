package teams.teams.application.mapper;

import teams.teams.api.model.CardRequestDto;
import teams.teams.api.model.CardResponseDto;
import teams.teams.domain.model.Card;

import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between domain models and DTOs for Card operations.
 */
public class CardDomainMapper {

    public static Card toDomain(CardRequestDto dto) {
        return Card.create(
                dto.getTitle(),
                dto.getDescription(),
                dto.getPosterUrl(),
                dto.getTeamId(),
                dto.getOwnerId()
        );
    }

    public static CardResponseDto toDto(Card card) {
        CardResponseDto dto = new CardResponseDto();
        dto.setId(card.getId());
        dto.setTitle(card.getTitle());
        dto.setDescription(card.getDescription());
        dto.setPosterUrl(card.getPosterUrl());
        dto.setOwnerId(card.getOwnerId());
        dto.setTeamId(card.getTeamId());
        if (card.getCreatedAt() != null) {
            dto.setCreatedAt(card.getCreatedAt().atZone(ZoneId.systemDefault()).toOffsetDateTime());
        }
        dto.setCreatedBy(card.getCreatedBy());
        if (card.getUpdatedAt() != null) {
            dto.setUpdatedAt(card.getUpdatedAt().atZone(ZoneId.systemDefault()).toOffsetDateTime());
        }
        dto.setUpdatedBy(card.getUpdatedBy());
        return dto;
    }

    public static List<CardResponseDto> toDtoList(List<Card> cards) {
        return cards.stream()
                .map(CardDomainMapper::toDto)
                .collect(Collectors.toList());
    }
}
