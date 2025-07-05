package org.users.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "TeamMemberResponse",
        description = "Schema to hold Team Member response information"
)
public class TeamMemberResponseDto {

    @Schema(
            description = "Team member identifier", 
            example = "1"
    )
    private Long id;

    @Schema(
            description = "ID of the card/project", 
            example = "1"
    )
    private Long cardId;

    @Schema(
            description = "ID of the user", 
            example = "1"
    )
    private Long userId;

    @Schema(
            description = "Role of the user in the team", 
            example = "PARTICIPANT"
    )
    private String role;

    @Schema(
            description = "Status of participation", 
            example = "JOINED"
    )
    private String status;

    @Schema(
            description = "Date and time when the user joined the team",
            example = "2023-01-01T10:00:00"
    )
    private LocalDateTime joinedAt;

    @Schema(
            description = "Date and time when the team member was created",
            example = "2023-01-01T10:00:00"
    )
    private LocalDateTime createdAt;

    @Schema(
            description = "User who created the team member",
            example = "admin"
    )
    private String createdBy;

    @Schema(
            description = "Date and time when the team member was last updated",
            example = "2023-01-02T11:00:00"
    )
    private LocalDateTime updatedAt;

    @Schema(
            description = "User who last updated the team member",
            example = "admin"
    )
    private String updatedBy;
}