package teams.teams.mapper;

import teams.teams.api.model.TeamMemberRequestDto;
import teams.teams.api.model.TeamMemberResponseDto;
import teams.teams.api.model.TeamResponseDto;
import teams.teams.entity.Team;
import teams.teams.entity.TeamMember;

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
        responseDto.setTeamId(teamMember.getTeamId());
        responseDto.setUserId(teamMember.getUserId());
        responseDto.setRole(teamMember.getRole());
        responseDto.setStatus(teamMember.getStatus());
        responseDto.setJoinedAt(OffsetDateTime.from(teamMember.getJoinedAt()));
        responseDto.setCreatedAt(OffsetDateTime.from(teamMember.getCreatedAt()));
        responseDto.setCreatedBy(teamMember.getCreatedBy());
        responseDto.setUpdatedAt(OffsetDateTime.from(teamMember.getUpdatedAt()));
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
    public static TeamMember mapToTeamMember(TeamMemberRequestDto requestDto, TeamMember teamMember) {
        teamMember.setCardId(requestDto.getCardId());
        teamMember.setTeamId(requestDto.getTeamId());
        teamMember.setUserId(requestDto.getUserId());
        teamMember.setRole(requestDto.getRole().getValue());
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

}