package teams.teams.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teams.teams.constants.TeamConstants;
import teams.teams.api.model.*;
import teams.teams.dto.UserDto;
import teams.teams.entity.Card;
import teams.teams.entity.Team;
import teams.teams.entity.TeamMember;
import teams.teams.exception.ResourceNotFoundException;
import teams.teams.mapper.TeamMapper;
import teams.teams.repository.CardsRepository;
import teams.teams.repository.TeamMemberRepository;
import teams.teams.repository.TeamRepository;
import teams.teams.service.ITeamService;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.criteria.Predicate;

@Service
@AllArgsConstructor
public class TeamServiceImpl implements ITeamService {

    private final TeamMemberRepository teamMemberRepository;
    private final TeamRepository teamRepository;
    private final CardsRepository cardsRepository;

    @Override
    @Transactional
    public TeamMemberResponseDto joinTeam(TeamMemberRequestDto teamMemberRequestDto) {
        // Check if the user is already a member of this team
        teamMemberRepository.findByTeamIdAndUserId(teamMemberRequestDto.getTeamId(), teamMemberRequestDto.getUserId())
                .ifPresent(existingMember -> {
                    throw new IllegalStateException("User is already a member or has a pending invitation");
                });

        // Create a new team member with PENDING status
        TeamMember teamMember = TeamMapper.mapToTeamMember(teamMemberRequestDto, new TeamMember());
        teamMember.setStatus(TeamConstants.STATUS_PENDING);
        
        TeamMember savedMember = teamMemberRepository.save(teamMember);
        return TeamMapper.mapToTeamMemberResponseDto(savedMember);
    }

    @Override
    public Team createTeam(TeamRequestDto teamMemberRequestDto) {
        Team team = new Team();
        team.setName(teamMemberRequestDto.getName());
        team.setDescription(teamMemberRequestDto.getDescription());
        return teamRepository.save(team);
    }

    @Override
    @Transactional
    public TeamMemberResponseDto inviteToTeam(Long cardId, TeamMemberRequestDto teamMemberRequestDto) {
        Card card = cardsRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "id", cardId.toString()));
        if (!card.getTeam().getId().equals(teamMemberRequestDto.getTeamId())) {
            throw new IllegalArgumentException("Card does not belong to the specified team");
        }
        teamMemberRequestDto.setCardId(cardId);
        teamMemberRequestDto.setTeamId(card.getTeam().getId());
        // Check if the user is already a member of this team
        teamMemberRepository.findByCardIdAndUserId(cardId, teamMemberRequestDto.getUserId())
                .ifPresent(existingMember -> {
                    throw new IllegalStateException("User is already a member or has a pending invitation");
                });

        // Create a new team member with PENDING status
        TeamMember teamMember = TeamMapper.mapToTeamMember(teamMemberRequestDto, new TeamMember());
        teamMember.setStatus(TeamConstants.STATUS_PENDING);
        
        TeamMember savedMember = teamMemberRepository.save(teamMember);
        return TeamMapper.mapToTeamMemberResponseDto(savedMember);
    }

    @Override
    @Transactional
    public TeamMemberResponseDto updateMemberStatus(Long cardId, Long userId, String status) {
        Card card = cardsRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "id", cardId.toString()));
        TeamMember teamMember = teamMemberRepository.findByCardIdAndUserId(cardId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("TeamMember", "cardId and userId",
                        cardId + " and " + userId));
        teamMember.setTeamId(card.getTeam().getId());

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
        cardsRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "id", cardId.toString()));
        TeamMember teamMember = teamMemberRepository.findByCardIdAndUserId(cardId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("TeamMember", "cardId and userId",
                        cardId + " and " + userId));

        teamMemberRepository.delete(teamMember);
        return true;
    }

    @Override
    public Page<TeamMemberResponseDto> getTeamMembers(Long cardId, Pageable pageable) {
        Specification<TeamMember> spec = (root, query, cb) -> {
            Predicate predicate = cb.equal(root.get("cardId"), cardId);
            return predicate;
        };
        
        Page<TeamMember> teamMembersPage = teamMemberRepository.findAll(spec, pageable);
        return teamMembersPage.map(TeamMapper::mapToTeamMemberResponseDto);
    }

    @Override
    public List<TeamMemberResponseDto> getTeamsByMember(Long userId) {
        return List.of();
    }

    @Override
    public TeamResponseDto fetchTeam(Long teamId) {
        return TeamMapper.mapToTeamResponseDto(teamRepository.findById(teamId).orElseThrow(() -> new ResourceNotFoundException("Team", "team Id",
                teamId.toString())));
    }
}