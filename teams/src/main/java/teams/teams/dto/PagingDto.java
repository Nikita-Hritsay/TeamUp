package teams.teams.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "Paging",
        description = "Schema to hold paginated response information"
)
public class PagingDto<T> {

    @Schema(
            description = "List of items in the current page",
            example = "[]"
    )
    private List<T> content;

    @Schema(
            description = "Total number of elements across all pages",
            example = "100"
    )
    private Long totalElements;

    @Schema(
            description = "Total number of pages",
            example = "10"
    )
    private Integer totalPages;

    @Schema(
            description = "Current page number (0-indexed)",
            example = "0"
    )
    private Integer number;

    @Schema(
            description = "Number of elements per page",
            example = "10"
    )
    private Integer size;
}
