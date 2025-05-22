package org.users.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.users.users.constants.UserConstants;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "Response",
        description = "Schema to hold successful response information"
)
public class ResponseDto
{

    @Schema(
            description = "Status code in the response", example = UserConstants.STATUS_200
    )
    private String statusCode;

    @Schema(
            description = "Status message in the response", example = UserConstants.MESSAGE_200
    )
    private String statusMessage;

}
