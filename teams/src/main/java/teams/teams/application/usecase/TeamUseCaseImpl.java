package teams.teams.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teams.teams.api.model.*;
import teams.teams.application.mapper.TeamDomainMapper;
import teams.teams.application.port.in.TeamUseCase;
import teams.teams.application.port.out.CardRepositoryPort;
import teams.teams.application.port.out.TeamMemberRepositoryPort;
import teams.teams.application.port.out.TeamRepositoryPort;
import teams.teams.constants.TeamConstants;
import teams.teams.domain.model.Card;
import teams.teams.domain.model.Team;
import teams.teams.domain.model.TeamMember;
import teams.teams.exception.ResourceNotFoundException;

import java.util.List;

/**
 * Implementation of TeamUseCase - orchestrates team-related business operations.
 */
@Service
@RequiredArgsConstructor
public class TeamUseCaseImpl implements TeamUseCase {

    private final TeamMemberRepositoryPort teamMemberRepository;
    private final TeamRepositoryPort teamRepository;
    private final CardRepositoryPort cardRepository;

    @Override
    @Transactional
    public TeamMemberResponseDto joinTeam(TeamMemberRequestDto teamMemberRequestDto) {
        // Check if the user is already a member of this team
        teamMemberRepository.findByTeamIdAndUserId(
                teamMemberRequestDto.getTeamId(),
                teamMemberRequestDto.getUserId()
        ).ifPresent(existingMember -> {
            throw new IllegalStateException("User is already a member or has a pending invitation");
        });

        // Create a new team member with PENDING status
        TeamMember teamMember = TeamDomainMapper.toDomain(teamMemberRequestDto);
        TeamMember savedMember = teamMemberRepository.save(teamMember);
        return TeamDomainMapper.toDto(savedMember);
    }

    @Override
    @Transactional
    public TeamResponseDto createTeam(TeamRequestDto teamRequestDto) {
        Team team = TeamDomainMapper.toDomain(teamRequestDto);
        Team savedTeam = teamRepository.save(team);
        return TeamDomainMapper.toDto(savedTeam);
    }

    @Override
    @Transactional
    public TeamMemberResponseDto inviteToTeam(Long cardId, TeamMemberRequestDto teamMemberRequestDto) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "id", cardId.toString()));
        
        if (!card.getTeamId().equals(teamMemberRequestDto.getTeamId())) {
            throw new IllegalArgumentException("Card does not belong to the specified team");
        }
        
        teamMemberRequestDto.setCardId(cardId);
        teamMemberRequestDto.setTeamId(card.getTeamId());
        
        // Check if the user is already a member of this team
        teamMemberRepository.findByCardIdAndUserId(cardId, teamMemberRequestDto.getUserId())
                .ifPresent(existingMember -> {
                    throw new IllegalStateException("User is already a member or has a pending invitation");
                });

        // Create a new team member with PENDING status
        TeamMember teamMember = TeamDomainMapper.toDomain(teamMemberRequestDto);
        TeamMember savedMember = teamMemberRepository.save(teamMember);
        return TeamDomainMapper.toDto(savedMember);
    }

    @Override
    @Transactional
    public TeamMemberResponseDto updateMemberStatus(Long cardId, Long userId, String status) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "id", cardId.toString()));
        
        TeamMember teamMember = teamMemberRepository.findByCardIdAndUserId(cardId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("TeamMember", "cardId and userId",
                        cardId + " and " + userId));
        
        teamMember.updateTeamId(card.getTeamId());

        if (TeamConstants.STATUS_JOINED.equals(status)) {
            teamMember.join();
        } else if (TeamConstants.STATUS_REJECTED.equals(status)) {
            teamMember.reject();
        } else {
            throw new IllegalArgumentException("Invalid status: " + status);
        }

        TeamMember updatedMember = teamMemberRepository.save(teamMember);
        return TeamDomainMapper.toDto(updatedMember);
    }

    @Override
    @Transactional
    public boolean removeMember(Long cardId, Long userId) {
        cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "id", cardId.toString()));
        
        TeamMember teamMember = teamMemberRepository.findByCardIdAndUserId(cardId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("TeamMember", "cardId and userId",
                        cardId + " and " + userId));

        teamMemberRepository.delete(teamMember);
        return true;
    }

    @Override
    public Page<TeamMemberResponseDto> getTeamMembers(Long cardId, Pageable pageable) {
        Page<TeamMember> teamMembersPage = teamMemberRepository.findByCardId(cardId, pageable);
        return teamMembersPage.map(TeamDomainMapper::toDto);
    }

    @Override
    public List<TeamMemberResponseDto> getTeamsByMember(Long userId) {
        // TODO: Implement if needed
        return List.of();
    }

    @Override
    public TeamResponseDto fetchTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team", "team Id", teamId.toString()));
        return TeamDomainMapper.toDto(team);
    }
}
