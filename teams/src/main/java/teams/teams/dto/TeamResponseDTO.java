package teams.teams.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import teams.teams.entity.TeamMember;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "TeamRequest",
        description = "Schema to hold Team response information"
)
public class TeamResponseDTO {

    @Schema(
            description = "ID of the team",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Name of the team",
            example = "Test Name"
    )
    private String name;

    @Schema(
            description = "Description of the team",
            example = "Test Description"
    )
    private String description;

    @Schema(
            description = "Team Members of the team",
            example = "[1, 2, 3, 4]"
    )
    private List<TeamMember> teamMembers;
}
