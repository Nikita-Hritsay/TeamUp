package org.accounts.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        name = "Customer",
        description = "Schema to hold Customer and Account information"
)
public class CustomerDto {

    @Schema(
            description = "First name of the customer", example = "Mykyta"
    )
    @NotEmpty(message = "First Name cannot be null or empty")
    @Size(min = 2, max = 30, message = "First Name cannot be longer than 30 and less than 2")
    private String firstName;

    @Schema(
            description = "Last name of the customer", example = "Hrytsai"
    )
    @NotEmpty(message = "Last Name cannot be null or empty")
    @Size(min = 2, max = 30, message = "Last Name cannot be longer than 30 and less than 2")
    private String lastName;

    @Schema(
            description = "Email of the customer", example = "hritsaynikita@gmail.com"
    )
    @NotEmpty(message = "Email cannot be null or empty")
    @Email(message = "Email address should be a valid format")
    private String email;

    @Schema(
            description = "Mobile number of the customer", example = "0501326581"
    )
    @NotEmpty(message = "Mobile number cannot be empty or null")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile Number should be 10 digit")
    private String mobileNumber;

    @Schema(
            description = "Account of the customer"
    )
    private AccountsDto accounts;

}
