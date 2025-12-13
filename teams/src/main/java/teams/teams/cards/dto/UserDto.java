package teams.teams.cards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import teams.teams.dto.TeamMemberResponseDto;

import java.util.List;
import java.util.Set;

@Data
@Schema(
        name = "User",
        description = "Schema to hold User information"
)
public class UserDto {

    @Schema(
            description = "Id of the user", example = "1"
    )
    private Long Id;

    @Schema(
            description = "First name of the user", example = "Mykyta"
    )
    @NotEmpty(message = "First Name cannot be null or empty")
    @Size(min = 2, max = 30, message = "First Name cannot be longer than 30 and less than 2")
    private String firstName;

    @Schema(
            description = "Last name of the user", example = "Hrytsai"
    )
    @NotEmpty(message = "Last Name cannot be null or empty")
    @Size(min = 2, max = 30, message = "Last Name cannot be longer than 30 and less than 2")
    private String lastName;

    @Schema(
            description = "Email of the user", example = "hritsaynikita@gmail.com"
    )
    @NotEmpty(message = "Email cannot be null or empty")
    @Email(message = "Email address should be a valid format")
    private String email;

    @Schema(
            description = "Mobile number of the user", example = "0501326581"
    )
    @NotEmpty(message = "Mobile number cannot be empty or null")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile Number should be 10 digit")
    private String mobileNumber;

    @Schema(
            description = "Roles of the user"
    )
    private Set<RoleDto> roles;

    @Schema(
            description = "Users teams"
    )
    private TeamMemberResponseDto teamMember;

    @Schema(
            description = "Users cards"
    )
    private List<CardResponseDto> cards;

}
