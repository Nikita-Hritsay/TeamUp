package org.products.products.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(
        name = "Category Response Dto",
        description = "Schema to hold Category Response information"
)
public class CategoryResponseDto {

    @Schema(
            description = "Id of the category", example = "1"
    )
    private Long id;

    @Schema(
            description = "Name of the category", example = "Motherboard"
    )
    private String name;

    @Schema(
            description = "Description of the category", example = "This motherboard has ..."
    )
    private String description;
}
