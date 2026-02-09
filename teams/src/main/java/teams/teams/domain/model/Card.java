package teams.teams.domain.model;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Domain model representing a Card aggregate root.
 * This is a pure domain model without any framework dependencies.
 */
public class Card {
    private Long id;
    private String title;
    private String description;
    private URI posterUrl;
    private Long teamId;
    private Long ownerId;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    // Private constructor to enforce creation through factory methods
    private Card() {
    }

    public static Card create(String title, String description, URI posterUrl, Long teamId, Long ownerId) {
        Card card = new Card();
        card.title = title;
        card.description = description;
        card.posterUrl = posterUrl;
        card.teamId = teamId;
        card.ownerId = ownerId;
        return card;
    }

    public static Card fromPersistence(Long id, String title, String description, URI posterUrl,
                                       Long teamId, Long ownerId,
                                       LocalDateTime createdAt, String createdBy,
                                       LocalDateTime updatedAt, String updatedBy) {
        Card card = new Card();
        card.id = id;
        card.title = title;
        card.description = description;
        card.posterUrl = posterUrl;
        card.teamId = teamId;
        card.ownerId = ownerId;
        card.createdAt = createdAt;
        card.createdBy = createdBy;
        card.updatedAt = updatedAt;
        card.updatedBy = updatedBy;
        return card;
    }

    public void update(String title, String description, URI posterUrl, Long teamId) {
        this.title = title;
        this.description = description;
        this.posterUrl = posterUrl;
        this.teamId = teamId;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public URI getPosterUrl() {
        return posterUrl;
    }

    public Long getTeamId() {
        return teamId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(id, card.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
