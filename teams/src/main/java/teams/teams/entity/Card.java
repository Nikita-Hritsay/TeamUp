package teams.teams.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity(name = "cards")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Card extends BaseEntity {

    @NotEmpty(message = "Title cannot be null or empty")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    @Column(name = "title")
    private String title;

    @Size(max = 500, message = "Description cannot be longer than 500 characters")
    @Column(name = "description")
    private String description;

    @Column(name = "poster_url")
    private String posterUrl;

    @NotNull(message = "Team ID cannot be null")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    @ToString.Exclude
    private Team team;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;
}
