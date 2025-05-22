package org.products.products.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        name = "Category Request Dto",
        description = "Schema to hold Category Request information"
)
public class CategoryRequestDto {

    @Schema(
            description = "Id of the category", example = "1"
    )
    private Long id;

    @Schema(
            description = "Name of the category", example = "Motherboard"
    )
    @NotEmpty(message = "Name cannot be null or empty")
    @Size(min = 2, max = 30, message = "Name cannot be longer than 50 and less than 10")
    private String name;

    @Schema(
            description = "Description of the category", example = "This motherboard has ..."
    )
    @NotEmpty(message = "Description cannot be null or empty")
    @Size(min = 2, max = 30, message = "Description  cannot be longer than 255 and less than 10")
    private String description;
}

