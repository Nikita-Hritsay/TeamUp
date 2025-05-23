package cards.cards.mapper;

import cards.cards.dto.CardRequestDto;
import cards.cards.dto.CardResponseDto;
import cards.cards.entity.Card;

public class CardMapper {

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
        responseDto.setPosterUrl(card.getPosterUrl());
        responseDto.setOwnerId(card.getOwnerId());
        responseDto.setCreatedAt(card.getCreatedAt());
        responseDto.setCreatedBy(card.getCreatedBy());
        responseDto.setUpdatedAt(card.getUpdatedAt());
        responseDto.setUpdatedBy(card.getUpdatedBy());
        return responseDto;
    }

    /**
     * Maps CardRequestDto to Card entity
     * 
     * @param requestDto CardRequestDto to map
     * @param card Card entity to update (can be a new instance)
     * @return Card entity with mapped values
     */
    public static Card mapToCard(CardRequestDto requestDto, Card card) {
        card.setTitle(requestDto.getTitle());
        card.setDescription(requestDto.getDescription());
        card.setPosterUrl(requestDto.getPosterUrl());
        card.setOwnerId(requestDto.getOwnerId());
        return card;
    }
}