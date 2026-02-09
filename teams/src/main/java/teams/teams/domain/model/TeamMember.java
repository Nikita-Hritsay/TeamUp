package teams.teams.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Domain model representing a TeamMember aggregate root.
 * This is a pure domain model without any framework dependencies.
 */
public class TeamMember {
    private Long id;
    private Long teamId;
    private Long cardId;
    private Long userId;
    private String role;
    private MemberStatus status;
    private LocalDateTime joinedAt;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    // Private constructor to enforce creation through factory methods
    private TeamMember() {
    }

    public static TeamMember create(Long teamId, Long cardId, Long userId, String role) {
        TeamMember member = new TeamMember();
        member.teamId = teamId;
        member.cardId = cardId;
        member.userId = userId;
        member.role = role;
        member.status = MemberStatus.PENDING;
        return member;
    }

    public static TeamMember fromPersistence(Long id, Long teamId, Long cardId, Long userId,
                                             String role, String status,
                                             LocalDateTime joinedAt,
                                             LocalDateTime createdAt, String createdBy,
                                             LocalDateTime updatedAt, String updatedBy) {
        TeamMember member = new TeamMember();
        member.id = id;
        member.teamId = teamId;
        member.cardId = cardId;
        member.userId = userId;
        member.role = role;
        member.status = MemberStatus.fromString(status);
        member.joinedAt = joinedAt;
        member.createdAt = createdAt;
        member.createdBy = createdBy;
        member.updatedAt = updatedAt;
        member.updatedBy = updatedBy;
        return member;
    }

    public void join() {
        this.status = MemberStatus.JOINED;
        this.joinedAt = LocalDateTime.now();
    }

    public void reject() {
        this.status = MemberStatus.REJECTED;
    }

    public void updateTeamId(Long teamId) {
        this.teamId = teamId;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getTeamId() {
        return teamId;
    }

    public Long getCardId() {
        return cardId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public String getStatusAsString() {
        return status.getValue();
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
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
        TeamMember that = (TeamMember) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Value object representing member status
     */
    public enum MemberStatus {
        PENDING("PENDING"),
        JOINED("JOINED"),
        REJECTED("REJECTED");

        private final String value;

        MemberStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static MemberStatus fromString(String status) {
            for (MemberStatus s : MemberStatus.values()) {
                if (s.value.equals(status)) {
                    return s;
                }
            }
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }
}
