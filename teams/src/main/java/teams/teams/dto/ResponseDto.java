package teams.teams.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(
        name = "Response",
        description = "Schema to hold successful response information"
)
public class ResponseDto {

    @Schema(
            description = "Status code of the response",
            example = "200"
    )
    private String statusCode;

    @Schema(
            description = "Status message of the response",
            example = "Request processed successfully"
    )
    private String statusMessage;

    public ResponseDto(String statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }
}
