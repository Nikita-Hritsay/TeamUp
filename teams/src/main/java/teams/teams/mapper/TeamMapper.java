package teams.teams.mapper;

import teams.teams.dto.TeamMemberRequestDto;
import teams.teams.dto.TeamMemberResponseDto;
import teams.teams.entity.TeamMember;

public class TeamMapper {

    /**
     * Maps TeamMember entity to TeamMemberResponseDto
     * 
     * @param teamMember TeamMember entity to map
     * @return TeamMemberResponseDto with mapped values
     */
    public static TeamMemberResponseDto mapToTeamMemberResponseDto(TeamMember teamMember) {
        TeamMemberResponseDto responseDto = new TeamMemberResponseDto();
        responseDto.setId(teamMember.getId());
        responseDto.setCardId(teamMember.getCardId());
        responseDto.setUserId(teamMember.getUserId());
        responseDto.setRole(teamMember.getRole());
        responseDto.setStatus(teamMember.getStatus());
        responseDto.setJoinedAt(teamMember.getJoinedAt());
        responseDto.setCreatedAt(teamMember.getCreatedAt());
        responseDto.setCreatedBy(teamMember.getCreatedBy());
        responseDto.setUpdatedAt(teamMember.getUpdatedAt());
        responseDto.setUpdatedBy(teamMember.getUpdatedBy());
        return responseDto;
    }

    /**
     * Maps TeamMemberRequestDto to TeamMember entity
     * 
     * @param requestDto TeamMemberRequestDto to map
     * @param cardId ID of the card/project
     * @param teamMember TeamMember entity to update (can be a new instance)
     * @return TeamMember entity with mapped values
     */
    public static TeamMember mapToTeamMember(TeamMemberRequestDto requestDto, Long cardId, TeamMember teamMember) {
        teamMember.setCardId(cardId);
        teamMember.setUserId(requestDto.getUserId());
        teamMember.setRole(requestDto.getRole());
        return teamMember;
    }
}