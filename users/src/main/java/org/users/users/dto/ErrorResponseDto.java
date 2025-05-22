package org.users.users.dto;

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
            description = "Error code representing the error happened"
    )
    private HttpStatus errorCode;

    @Schema(
            description = "API path invoked by client"
    )
    private String apiPath;


    @Schema(
            description = "Status message of the error"
    )
    private String statusMessage;


    @Schema(
            description = "Time of the error"
    )
    private LocalDateTime errorTime;

}
