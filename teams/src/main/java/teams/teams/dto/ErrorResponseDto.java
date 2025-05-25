package teams.teams.dto;

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
            description = "API path that resulted in an error",
            example = "/api/v1/teams/1/join"
    )
    private String apiPath;

    @Schema(
            description = "Error code",
            example = "404"
    )
    private HttpStatus errorCode;

    @Schema(
            description = "Error message",
            example = "Team member not found"
    )
    private String errorMessage;

    @Schema(
            description = "Time of the error",
            example = "2023-01-01T10:00:00"
    )
    private LocalDateTime errorTime;
}