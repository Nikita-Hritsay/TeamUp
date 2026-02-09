package teams.teams.adapters.out.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import teams.teams.application.port.out.TeamMemberRepositoryPort;
import teams.teams.domain.model.TeamMember;
import teams.teams.adapters.out.persistence.entity.TeamMemberJpaEntity;
import teams.teams.adapters.out.persistence.mapper.TeamMemberPersistenceMapper;
import teams.teams.adapters.out.persistence.repository.TeamMemberJpaRepository;

import jakarta.persistence.criteria.Predicate;
import java.util.Optional;

/**
 * Adapter implementing TeamMemberRepositoryPort using Spring Data JPA.
 */
@Component
@RequiredArgsConstructor
public class TeamMemberRepositoryAdapter implements TeamMemberRepositoryPort {

    private final TeamMemberJpaRepository jpaRepository;

    @Override
    public TeamMember save(TeamMember teamMember) {
        TeamMemberJpaEntity entity = TeamMemberPersistenceMapper.toEntity(teamMember);
        TeamMemberJpaEntity savedEntity = jpaRepository.save(entity);
        return TeamMemberPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<TeamMember> findByCardIdAndUserId(Long cardId, Long userId) {
        return jpaRepository.findByCardIdAndUserId(cardId, userId)
                .map(TeamMemberPersistenceMapper::toDomain);
    }

    @Override
    public Optional<TeamMember> findByTeamIdAndUserId(Long teamId, Long userId) {
        return jpaRepository.findByTeamIdAndUserId(teamId, userId)
                .map(TeamMemberPersistenceMapper::toDomain);
    }

    @Override
    public Page<TeamMember> findByCardId(Long cardId, Pageable pageable) {
        Specification<TeamMemberJpaEntity> spec = (root, query, cb) -> {
            Predicate predicate = cb.equal(root.get("cardId"), cardId);
            return predicate;
        };
        
        return jpaRepository.findAll(spec, pageable)
                .map(TeamMemberPersistenceMapper::toDomain);
    }

    @Override
    public void delete(TeamMember teamMember) {
        TeamMemberJpaEntity entity = TeamMemberPersistenceMapper.toEntity(teamMember);
        jpaRepository.delete(entity);
    }
}
