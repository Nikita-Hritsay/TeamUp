package teams.teams.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "TeamMemberRequest",
        description = "Schema to hold Team Member request information"
)
public class TeamMemberRequestDto {

    @Schema(
            description = "ID of the user to add to the team", 
            example = "1"
    )
    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @Schema(
            description = "Role of the user in the team", 
            example = "PARTICIPANT",
            allowableValues = {"CREATOR", "PARTICIPANT", "LEADER"}
    )
    @NotNull(message = "Role cannot be null")
    private String role;

    @Schema(
            description = "Team Id where user is added",
            example = "1"
    )
    @NotNull(message = "Role cannot be null")
    private Long teamId;

    @Schema(
            description = "Card Id from which user added himself",
            example = "1"
    )
    private Long cardId;
}