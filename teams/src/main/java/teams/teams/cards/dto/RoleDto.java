package teams.teams.cards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Schema(
        name = "Role",
        description = "Schema to hold Role information"
)
public class RoleDto {

    @NotEmpty(message = "Role Name cannot be empty or null")
    @Schema(
            description = "Role type information"
    )
    private String roleName;

}
