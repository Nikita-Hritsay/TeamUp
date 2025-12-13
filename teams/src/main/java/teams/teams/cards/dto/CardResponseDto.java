package teams.teams.cards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "CardResponse",
        description = "Schema to hold Card response information"
)
public class CardResponseDto {

    @Schema(
            description = "Card identifier",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Title of the card",
            example = "Project X"
    )
    private String title;

    @Schema(
            description = "Description of the card",
            example = "This is a project about X technology"
    )
    private String description;

    @Schema(
            description = "URL to the poster image",
            example = "https://example.com/image.jpg"
    )
    private String posterUrl;

    @Schema(
            description = "ID of the card owner",
            example = "1"
    )
    private Long ownerId;

    @Schema(
            description = "Date and time when the card was created",
            example = "2023-01-01T10:00:00"
    )
    private LocalDateTime createdAt;

    @Schema(
            description = "User who created the card",
            example = "admin"
    )
    private String createdBy;

    @Schema(
            description = "Date and time when the card was last updated",
            example = "2023-01-02T11:00:00"
    )
    private LocalDateTime updatedAt;

    @Schema(
            description = "User who last updated the card",
            example = "admin"
    )
    private String updatedBy;
}
