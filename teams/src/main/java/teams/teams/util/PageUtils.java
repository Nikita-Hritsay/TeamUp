package teams.teams.util;

import org.springframework.data.domain.Page;
import teams.teams.api.model.CardResponseDto;
import teams.teams.api.model.PagingCardResponseDto;
import teams.teams.api.model.PagingTeamMemberResponseDto;
import teams.teams.api.model.TeamMemberResponseDto;

public class PageUtils {

    private PageUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Converts a Spring Page of CardResponseDto to PagingCardResponseDto
     *
     * @param page the Spring Page object containing cards
     * @return PagingCardResponseDto containing the paginated card data
     */
    public static PagingCardResponseDto toPagingCardResponseDto(Page<CardResponseDto> page) {
        PagingCardResponseDto pagingDto = new PagingCardResponseDto();
        pagingDto.setContent(page.getContent());
        pagingDto.setTotalElements(page.getTotalElements());
        pagingDto.setTotalPages(page.getTotalPages());
        pagingDto.setNumber(page.getNumber());
        pagingDto.setSize(page.getSize());
        return pagingDto;
    }

    /**
     * Converts a Spring Page of TeamMemberResponseDto to PagingTeamMemberResponseDto
     *
     * @param page the Spring Page object containing team members
     * @return PagingTeamMemberResponseDto containing the paginated team member data
     */
    public static PagingTeamMemberResponseDto toPagingTeamMemberResponseDto(Page<TeamMemberResponseDto> page) {
        PagingTeamMemberResponseDto pagingDto = new PagingTeamMemberResponseDto();
        pagingDto.setContent(page.getContent());
        pagingDto.setTotalElements(page.getTotalElements());
        pagingDto.setTotalPages(page.getTotalPages());
        pagingDto.setNumber(page.getNumber());
        pagingDto.setSize(page.getSize());
        return pagingDto;
    }
}
