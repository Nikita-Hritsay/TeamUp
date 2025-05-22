package org.accounts.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.accounts.accounts.constants.AccountsConstants;
import org.accounts.accounts.entity.Account;

@Data
@AllArgsConstructor
@Schema(
        name = "Response",
        description = "Schema to hold successful response information"
)
public class ResponseDto {

    @Schema(
            description = "Status code in the response", example = AccountsConstants.STATUS_200
    )
    private String statusCode;

    @Schema(
            description = "Status message in the response", example = AccountsConstants.MESSAGE_200
    )
    private String statusMessage;

}
