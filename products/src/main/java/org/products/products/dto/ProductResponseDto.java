package org.products.products.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(
        name = "Product Response Dto",
        description = "Schema to hold Product Response information"
)
public class ProductResponseDto {

    @Schema(
            description = "Name of the Product", example = "Nvidia Ge-Force 4060"
    )
    private String name;

    @Schema(
            description = "Description of the Product", example = "Super cool graphics card ..."
    )
    private String description;

    @Schema(
            description = "Price of the Product", example = "14999"
    )
    private int price;

    @Schema(
            description = "Seller id of the Product", example = "1"
    )
    private int seller_id;

    @Schema(
            description = "CategoryDto of the Product"
    )
    private CategoryResponseDto category;

}
