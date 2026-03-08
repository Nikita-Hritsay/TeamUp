package teams.teams.mapper;

import teams.teams.api.model.TeamMemberRequestDto;
import teams.teams.api.model.TeamMemberResponseDto;
import teams.teams.api.model.TeamResponseDto;
import teams.teams.entity.Card;
import teams.teams.entity.Team;
import teams.teams.entity.TeamMember;

import java.time.ZoneId;
import java.util.Collections;
import java.time.OffsetDateTime;

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
        responseDto.setStatus(teamMember.getStatus());
        responseDto.setCreatedAt(teamMember.getCreatedAt().atZone(ZoneId.systemDefault()).toOffsetDateTime());
        responseDto.setCreatedBy(teamMember.getCreatedBy());
        responseDto.setUpdatedAt(teamMember.getUpdatedAt().atZone(ZoneId.systemDefault()).toOffsetDateTime());
        responseDto.setUpdatedBy(teamMember.getUpdatedBy());
        return responseDto;
    }

    /**
     * Maps TeamMemberRequestDto to TeamMember entity
     * 
     * @param requestDto TeamMemberRequestDto to map
     * @param teamMember TeamMember entity to update (can be a new instance)
     * @return TeamMember entity with mapped values
     */
    public static TeamMember mapToTeamMember(TeamMemberRequestDto requestDto, Card card, TeamMember teamMember) {
        teamMember.setCardId(requestDto.getCardId());
        teamMember.setTeamId(card.getTeam().getId());
        teamMember.setUserId(requestDto.getUserId());
        return teamMember;
    }

    public static TeamResponseDto mapToTeamResponseDto(Team team) {
        TeamResponseDto responseDto = new TeamResponseDto();
        responseDto.setId(team.getId());
        responseDto.setName(team.getName());
        responseDto.setDescription(team.getDescription());
        responseDto.setTeamMembers(team.getTeamMembers().stream().map(TeamMapper::mapToTeamMemberResponseDto).toList());
        return responseDto;
    }

    /**
     * Maps Team to TeamResponseDto without loading team members (for list views).
     */
    public static TeamResponseDto mapToTeamResponseDtoSummary(Team team) {
        TeamResponseDto responseDto = new TeamResponseDto();
        responseDto.setId(team.getId());
        responseDto.setName(team.getName());
        responseDto.setDescription(team.getDescription());
        responseDto.setTeamMembers(Collections.emptyList());
        return responseDto;
    }

}