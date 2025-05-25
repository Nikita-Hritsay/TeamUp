package teams.teams.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teams.teams.constants.TeamConstants;
import teams.teams.dto.TeamMemberRequestDto;
import teams.teams.dto.TeamMemberResponseDto;
import teams.teams.entity.TeamMember;
import teams.teams.exception.ResourceNotFoundException;
import teams.teams.mapper.TeamMapper;
import teams.teams.repository.TeamMemberRepository;
import teams.teams.service.ITeamService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TeamServiceImpl implements ITeamService {

    private final TeamMemberRepository teamMemberRepository;

    @Override
    @Transactional
    public TeamMemberResponseDto joinTeam(Long cardId, TeamMemberRequestDto teamMemberRequestDto) {
        // Check if the user is already a member of this team
        teamMemberRepository.findByCardIdAndUserId(cardId, teamMemberRequestDto.getUserId())
                .ifPresent(existingMember -> {
                    throw new IllegalStateException("User is already a member or has a pending invitation");
                });

        // Create a new team member with PENDING status
        TeamMember teamMember = TeamMapper.mapToTeamMember(teamMemberRequestDto, cardId, new TeamMember());
        teamMember.setStatus(TeamConstants.STATUS_PENDING);
        
        TeamMember savedMember = teamMemberRepository.save(teamMember);
        return TeamMapper.mapToTeamMemberResponseDto(savedMember);
    }

    @Override
    @Transactional
    public TeamMemberResponseDto inviteToTeam(Long cardId, TeamMemberRequestDto teamMemberRequestDto) {
        // Check if the user is already a member of this team
        teamMemberRepository.findByCardIdAndUserId(cardId, teamMemberRequestDto.getUserId())
                .ifPresent(existingMember -> {
                    throw new IllegalStateException("User is already a member or has a pending invitation");
                });

        // Create a new team member with PENDING status
        TeamMember teamMember = TeamMapper.mapToTeamMember(teamMemberRequestDto, cardId, new TeamMember());
        teamMember.setStatus(TeamConstants.STATUS_PENDING);
        
        TeamMember savedMember = teamMemberRepository.save(teamMember);
        return TeamMapper.mapToTeamMemberResponseDto(savedMember);
    }

    @Override
    @Transactional
    public TeamMemberResponseDto updateMemberStatus(Long cardId, Long userId, String status) {
        TeamMember teamMember = teamMemberRepository.findByCardIdAndUserId(cardId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("TeamMember", "cardId and userId", 
                        cardId + " and " + userId));

        if (TeamConstants.STATUS_JOINED.equals(status)) {
            teamMember.join(); // Sets status to JOINED and updates joinedAt
        } else if (TeamConstants.STATUS_REJECTED.equals(status)) {
            teamMember.reject(); // Sets status to REJECTED
        } else {
            throw new IllegalArgumentException("Invalid status: " + status);
        }

        TeamMember updatedMember = teamMemberRepository.save(teamMember);
        return TeamMapper.mapToTeamMemberResponseDto(updatedMember);
    }

    @Override
    @Transactional
    public boolean removeMember(Long cardId, Long userId) {
        TeamMember teamMember = teamMemberRepository.findByCardIdAndUserId(cardId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("TeamMember", "cardId and userId", 
                        cardId + " and " + userId));

        teamMemberRepository.delete(teamMember);
        return true;
    }

    @Override
    public List<TeamMemberResponseDto> getTeamMembers(Long cardId) {
        List<TeamMember> teamMembers = teamMemberRepository.findByCardId(cardId);
        return teamMembers.stream()
                .map(TeamMapper::mapToTeamMemberResponseDto)
                .collect(Collectors.toList());
    }
}