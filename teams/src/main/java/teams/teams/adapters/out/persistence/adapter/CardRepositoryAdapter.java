package teams.teams.adapters.out.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import teams.teams.application.port.out.CardRepositoryPort;
import teams.teams.domain.model.Card;
import teams.teams.adapters.out.persistence.entity.CardJpaEntity;
import teams.teams.adapters.out.persistence.mapper.CardPersistenceMapper;
import teams.teams.adapters.out.persistence.repository.CardJpaRepository;
import teams.teams.adapters.out.persistence.repository.TeamJpaRepository;
import teams.teams.adapters.out.persistence.entity.TeamJpaEntity;
import teams.teams.adapters.out.persistence.specification.CardSpecification;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter implementing CardRepositoryPort using Spring Data JPA.
 */
@Component
@RequiredArgsConstructor
public class CardRepositoryAdapter implements CardRepositoryPort {

    private final CardJpaRepository cardJpaRepository;
    private final TeamJpaRepository teamJpaRepository;

    @Override
    public Card save(Card card) {
        TeamJpaEntity teamEntity = card.getTeamId() != null
                ? teamJpaRepository.findById(card.getTeamId()).orElse(null)
                : null;
        
        CardJpaEntity entity = CardPersistenceMapper.toEntity(card, teamEntity);
        CardJpaEntity savedEntity = cardJpaRepository.save(entity);
        return CardPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Card> findById(Long id) {
        return cardJpaRepository.findById(id)
                .map(CardPersistenceMapper::toDomain);
    }

    @Override
    public List<Card> findByOwnerId(Long ownerId) {
        return cardJpaRepository.findByOwnerId(ownerId).stream()
                .map(CardPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Card> findAll(Pageable pageable) {
        return cardJpaRepository.findAll(pageable)
                .map(CardPersistenceMapper::toDomain);
    }

    @Override
    public Page<Card> findByFilters(Long ownerId, String title, Pageable pageable) {
        var spec = CardSpecification.where(
                CardSpecification.hasOwnerId(ownerId),
                CardSpecification.titleContains(title)
        );
        
        return cardJpaRepository.findAll(spec, pageable)
                .map(CardPersistenceMapper::toDomain);
    }

    @Override
    public void delete(Card card) {
        TeamJpaEntity teamEntity = card.getTeamId() != null
                ? teamJpaRepository.findById(card.getTeamId()).orElse(null)
                : null;
        CardJpaEntity entity = CardPersistenceMapper.toEntity(card, teamEntity);
        cardJpaRepository.delete(entity);
    }

    @Override
    public boolean existsById(Long id) {
        return cardJpaRepository.existsById(id);
    }
}
