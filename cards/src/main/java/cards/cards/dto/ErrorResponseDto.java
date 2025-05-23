package cards.cards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Schema(
        name = "ErrorResponse",
        description = "Schema to hold error response information"
)
public class ErrorResponseDto {

    @Schema(
            description = "API path that caused the error",
            example = "/api/v1/cards/999"
    )
    private String apiPath;

    @Schema(
            description = "Error code",
            example = "404"
    )
    private HttpStatus errorCode;

    @Schema(
            description = "Error message",
            example = "Card not found with id : '999'"
    )
    private String errorMessage;

    @Schema(
            description = "Error time",
            example = "2023-01-01T10:00:00"
    )
    private LocalDateTime errorTime;
}