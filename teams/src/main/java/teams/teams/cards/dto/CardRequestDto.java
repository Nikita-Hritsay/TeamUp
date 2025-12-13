package teams.teams.cards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "CardRequest",
        description = "Schema to hold Card request information"
)
public class CardRequestDto {

    @Schema(
            description = "Title of the card",
            example = "Project X"
    )
    @NotEmpty(message = "Title cannot be null or empty")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @Schema(
            description = "Description of the card",
            example = "This is a project about X technology"
    )
    @Size(max = 500, message = "Description cannot be longer than 500 characters")
    private String description;

    @Schema(
            description = "URL to the poster image",
            example = "https://example.com/image.jpg"
    )
    @URL(message = "Poster URL must be a valid URL")
    private String posterUrl;

    @Schema(
            description = "ID of the card owner",
            example = "1"
    )
    @NotNull(message = "Owner ID cannot be null")
    private Long ownerId;
}
