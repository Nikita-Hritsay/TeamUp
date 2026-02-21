package teams.teams.adapters.out.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teams.teams.application.port.out.TeamRepositoryPort;
import teams.teams.domain.model.Team;
import teams.teams.adapters.out.persistence.entity.TeamJpaEntity;
import teams.teams.adapters.out.persistence.mapper.TeamPersistenceMapper;
import teams.teams.adapters.out.persistence.repository.TeamJpaRepository;

import java.util.Optional;

/**
 * Adapter implementing TeamRepositoryPort using Spring Data JPA.
 */
@Component
@RequiredArgsConstructor
public class TeamRepositoryAdapter implements TeamRepositoryPort {

    private final TeamJpaRepository jpaRepository;

    @Override
    public Team save(Team team) {
        TeamJpaEntity entity = TeamPersistenceMapper.toEntity(team);
        TeamJpaEntity savedEntity = jpaRepository.save(entity);
        return TeamPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Team> findById(Long id) {
        return jpaRepository.findById(id)
                .map(TeamPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
}
