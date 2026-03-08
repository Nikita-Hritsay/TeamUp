package teams.teams.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teams.teams.constants.TeamConstants;
import teams.teams.api.model.*;
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

@Service
@AllArgsConstructor
public class TeamServiceImpl implements ITeamService {

    private final TeamMemberRepository teamMemberRepository;
    private final TeamRepository teamRepository;
    private final CardsRepository cardsRepository;

    @Override
    @Transactional
    public TeamMemberResponseDto joinTeam(TeamMemberRequestDto teamMemberRequestDto) {
        teamMemberRepository.findByCardIdAndUserId(teamMemberRequestDto.getCardId(), teamMemberRequestDto.getUserId())
                .ifPresent(existingMember -> {
                    throw new IllegalStateException("User is already a member or has a pending invitation");
                });
        Card card = cardsRepository.findById(teamMemberRequestDto.getCardId()).orElseThrow(() ->
                new ResourceNotFoundException("Card", "id", teamMemberRequestDto.getCardId().toString()));
        TeamMember teamMember = TeamMapper.mapToTeamMember(teamMemberRequestDto, card, new TeamMember());
        teamMember.setRole(TeamConstants.ROLE_PARTICIPANT);
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
        teamMemberRequestDto.setCardId(cardId);
        teamMemberRepository.findByCardIdAndUserId(cardId, teamMemberRequestDto.getUserId())
                .ifPresent(existingMember -> {
                    throw new IllegalStateException("User is already a member or has a pending invitation");
                });
        TeamMember teamMember = TeamMapper.mapToTeamMember(teamMemberRequestDto, card, new TeamMember());
        teamMember.setRole(TeamConstants.ROLE_PARTICIPANT);
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
            teamMember.join();
        } else if (TeamConstants.STATUS_REJECTED.equals(status)) {
            teamMember.reject();
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
        Specification<TeamMember> spec = (root, query, cb) -> cb.equal(root.get("cardId"), cardId);
        
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

    @Override
    public Page<TeamResponseDto> listTeams(Pageable pageable, Long userId) {
        Page<Team> teamPage = userId != null
                ? teamRepository.findTeamsByMemberUserId(userId, pageable)
                : teamRepository.findAll(pageable);
        return teamPage.map(TeamMapper::mapToTeamResponseDtoSummary);
    }

    @Override
    public Page<TeamMemberResponseDto> getTeamMembersByTeamId(Long teamId, Pageable pageable) {
        teamRepository.findById(teamId).orElseThrow(() -> new ResourceNotFoundException("Team", "teamId", teamId.toString()));
        Page<TeamMember> memberPage = teamMemberRepository.findByTeamId(teamId, pageable);
        return memberPage.map(TeamMapper::mapToTeamMemberResponseDto);
    }
}