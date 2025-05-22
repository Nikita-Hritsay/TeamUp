package org.products.products.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        name = "Product Request Dto",
        description = "Schema to hold Product Request information"
)
public class ProductRequestDto {

    @Schema(
            description = "Name of the Product", example = "Nvidia Ge-Force 4060"
    )
    @NotEmpty(message = "Name cannot be null or empty")
    @Size(min = 2, max = 30, message = "Name cannot be longer than 50 and less than 10")
    private String name;

    @Schema(
            description = "Description of the Product", example = "Super cool graphics card ..."
    )
    @NotEmpty(message = "Description cannot be null or empty")
    @Size(min = 2, max = 30, message = "Description cannot be longer than 255 and less than 10")
    private String description;

    @Schema(
            description = "Price of the Product", example = "14999"
    )
    @NotEmpty(message = "Price cannot be null or empty")
    @Min(value = 100, message = "Price cannot be less than 30")
    private int price;

    @Schema(
            description = "Seller id of the Product", example = "1"
    )
    @NotEmpty(message = "Seller cannot be null or empty")
    @Min(value = 0, message = "Seller Id cannot be less than 0")
    private int seller_id;

    @Schema(
            description = "Category id of the Product", example = "1"
    )
    @NotEmpty(message = "Category id cannot be null or empty")
    @Min(value = 0, message = "Category Id cannot be less than 0")
    private int category_id;
}
