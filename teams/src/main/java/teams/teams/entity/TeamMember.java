package teams.teams.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import teams.teams.constants.TeamConstants;

import java.time.LocalDateTime;

@Entity(name = "team_members")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TeamMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Card ID cannot be null")
    @Column(name = "card_id", nullable = false)
    private Long cardId;

    @NotNull(message = "User ID cannot be null")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull(message = "Role cannot be null")
    @Column(name = "role", nullable = false)
    private String role;

    @NotNull(message = "Status cannot be null")
    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    /**
     * Updates the status to JOINED and sets the joinedAt timestamp
     */
    public void join() {
        this.status = TeamConstants.STATUS_JOINED;
        this.joinedAt = LocalDateTime.now();
    }

    /**
     * Updates the status to REJECTED
     */
    public void reject() {
        this.status = TeamConstants.STATUS_REJECTED;
    }
}