package teams.teams.adapters.out.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import teams.teams.adapters.out.persistence.entity.BaseJpaEntity;

import java.util.List;

/**
 * JPA entity for Team - used only in the persistence adapter layer.
 * This is separate from the domain model.
 */
@Entity(name = "team")
@Table(name = "team")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TeamJpaEntity extends BaseJpaEntity {

    @NotNull(message = "Team Name cannot be null")
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "team_members",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "team_member_id")
    )
    private List<TeamMemberJpaEntity> teamMembers;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<CardJpaEntity> cards;
}
