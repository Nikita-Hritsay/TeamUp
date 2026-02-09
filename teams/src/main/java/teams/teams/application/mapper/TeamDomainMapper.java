package teams.teams.application.mapper;

import teams.teams.api.model.*;
import teams.teams.domain.model.Team;
import teams.teams.domain.model.TeamMember;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.stream.Collectors;

/**
 * Mapper for converting between domain models and DTOs for Team operations.
 */
public class TeamDomainMapper {

    public static Team toDomain(TeamRequestDto dto) {
        return Team.create(dto.getName(), dto.getDescription());
    }

    public static TeamResponseDto toDto(Team team) {
        TeamResponseDto dto = new TeamResponseDto();
        dto.setId(team.getId());
        dto.setName(team.getName());
        dto.setDescription(team.getDescription());
        // Note: teamMembers would need to be loaded separately if needed
        return dto;
    }

    public static TeamMember toDomain(TeamMemberRequestDto dto) {
        return TeamMember.create(
                dto.getTeamId(),
                dto.getCardId(),
                dto.getUserId(),
                dto.getRole().getValue()
        );
    }

    public static TeamMemberResponseDto toDto(TeamMember member) {
        TeamMemberResponseDto dto = new TeamMemberResponseDto();
        dto.setId(member.getId());
        dto.setCardId(member.getCardId());
        dto.setTeamId(member.getTeamId());
        dto.setUserId(member.getUserId());
        dto.setRole(member.getRole());
        dto.setStatus(member.getStatusAsString());
        if (member.getJoinedAt() != null) {
            dto.setJoinedAt(member.getJoinedAt().atZone(ZoneId.systemDefault()).toOffsetDateTime());
        }
        if (member.getCreatedAt() != null) {
            dto.setCreatedAt(member.getCreatedAt().atZone(ZoneId.systemDefault()).toOffsetDateTime());
        }
        dto.setCreatedBy(member.getCreatedBy());
        if (member.getUpdatedAt() != null) {
            dto.setUpdatedAt(member.getUpdatedAt().atZone(ZoneId.systemDefault()).toOffsetDateTime());
        }
        dto.setUpdatedBy(member.getUpdatedBy());
        return dto;
    }
}
