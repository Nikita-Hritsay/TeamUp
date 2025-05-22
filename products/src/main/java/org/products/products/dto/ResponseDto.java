package org.products.products.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.products.products.constants.ProductsConstants;

@Data
@AllArgsConstructor
@Schema(
        name = "Response",
        description = "Schema to hold successful response information"
)
public class ResponseDto {

    @Schema(
            description = "Status code in the response", example = ProductsConstants.STATUS_200
    )
    private String statusCode;

    @Schema(
            description = "Status message in the response", example = ProductsConstants.MESSAGE_200
    )
    private String statusMessage;

}
