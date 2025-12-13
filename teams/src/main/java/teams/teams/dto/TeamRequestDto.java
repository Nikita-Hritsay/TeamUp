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
        name = "TeamRequest",
        description = "Schema to hold Team request information"
)
public class TeamRequestDto {

    @Schema(
            description = "ID of the team",
            example = "1"
    )
    private Long teamId;

    @Schema(
            description = "Name of the team",
            example = "Super cool team"
    )
    @NotNull(message = "Name cannot be null")
    private String name;

    @Schema(
            description = "Description of the team",
            example = "Description of team"
    )
    private String description;

}