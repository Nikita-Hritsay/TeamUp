package teams.teams.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Domain model representing a Team aggregate root.
 * This is a pure domain model without any framework dependencies.
 */
public class Team {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    // Private constructor to enforce creation through factory methods
    private Team() {
    }

    public static Team create(String name, String description) {
        Team team = new Team();
        team.name = name;
        team.description = description;
        return team;
    }

    public static Team fromPersistence(Long id, String name, String description,
                                       LocalDateTime createdAt, String createdBy,
                                       LocalDateTime updatedAt, String updatedBy) {
        Team team = new Team();
        team.id = id;
        team.name = name;
        team.description = description;
        team.createdAt = createdAt;
        team.createdBy = createdBy;
        team.updatedAt = updatedAt;
        team.updatedBy = updatedBy;
        return team;
    }

    public void update(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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
        Team team = (Team) o;
        return Objects.equals(id, team.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
