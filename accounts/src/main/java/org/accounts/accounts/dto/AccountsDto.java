package org.accounts.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(
        name = "Account",
        description = "Schema to hold Account information"
)
public class AccountsDto {

    @NotEmpty(message = "Account number cannot be empty or null")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile Number should be 10 digit")
    @Schema(
            description = "Account number information"
    )
    private Long accountNumber;

    @NotEmpty(message = "Account Type cannot be empty or null")
    @Schema(
            description = "Account type information"
    )
    private String accountType;

}
