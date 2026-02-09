package teams.teams.adapters.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import teams.teams.api.CardsApi;
import teams.teams.api.model.CardRequestDto;
import teams.teams.api.model.CardResponseDto;
import teams.teams.api.model.PagingCardResponseDto;
import teams.teams.api.model.ResponseDto;
import teams.teams.application.port.in.CardUseCase;
import teams.teams.constants.CardConstants;
import teams.teams.util.PageUtils;

import java.util.List;

/**
 * REST controller adapter for Card operations.
 * This is a thin adapter that delegates to the application layer.
 */
@Tag(
        name = "CRUD REST API for Cards",
        description = "Operations for managing project cards"
)
@RestController
@RequestMapping(path = "/api/v1/cards", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@RequiredArgsConstructor
public class CardsController implements CardsApi {

    private final CardUseCase cardUseCase;

    @Value("${build.version}")
    private String buildVersion;

    @Operation(
            summary = "Get Build Version",
            description = "Get the current build version of the service"
    )
    @GetMapping("/build-version")
    public ResponseEntity<String> getCardsBuildVersion() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buildVersion);
    }

    @Operation(
            summary = "Create Card REST API",
            description = "Create a new card"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status CREATED"
    )
    @PostMapping
    public ResponseEntity<ResponseDto> createCard(@Valid @RequestBody CardRequestDto cardRequestDto) {
        cardUseCase.createCard(cardRequestDto);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatusCode(CardConstants.STATUS_201);
        responseDto.setStatusMessage(CardConstants.MESSAGE_201);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDto);
    }

    @Operation(
            summary = "Get Card REST API",
            description = "Get a card by ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK"
    )
    @GetMapping("/fetch")
    public ResponseEntity<CardResponseDto> getCardById(@RequestParam Long cardId) {
        CardResponseDto cardResponseDto = cardUseCase.getCardById(cardId);
        return ResponseEntity.ok(cardResponseDto);
    }

    @Operation(
            summary = "Get Cards by user ID REST API",
            description = "Get a cards by user ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK"
    )
    @GetMapping("/fetchByUser")
    public ResponseEntity<List<CardResponseDto>> getCardsByUserId(@RequestParam Long userId) {
        List<CardResponseDto> cardResponseDto = cardUseCase.getCardsByUserId(userId);
        return ResponseEntity.ok(cardResponseDto);
    }

    @Operation(
            summary = "Get All Cards REST API",
            description = "Get all cards with pagination and optional filtering by ownerId or title"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK"
    )
    @GetMapping
    public ResponseEntity<PagingCardResponseDto> getAllCards(
            @RequestParam(required = false) Long ownerId,
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);

        // If filtering parameters are provided, use filtered search
        if (ownerId != null || (title != null && !title.isEmpty())) {
            Page<CardResponseDto> filteredCards = cardUseCase.getFilteredCards(ownerId, title, pageable);
            return ResponseEntity.ok(PageUtils.toPagingCardResponseDto(filteredCards));
        }

        // Otherwise, get all cards
        Page<CardResponseDto> cards = cardUseCase.getAllCards(pageable);
        return ResponseEntity.ok(PageUtils.toPagingCardResponseDto(cards));
    }

    @Operation(
            summary = "Update Card REST API",
            description = "Update an existing card"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Card not found",
                    content = @Content(
                            schema = @Schema(implementation = ResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ResponseDto.class)
                    )
            )
    })

    @PutMapping("/{cardId}")
    public ResponseEntity<CardResponseDto> updateCard(
            @PathVariable Long cardId,
            @Valid @RequestBody CardRequestDto cardRequestDto) {
        CardResponseDto updatedCard = cardUseCase.updateCard(cardId, cardRequestDto);
        return ResponseEntity.ok(updatedCard);
    }

    @Operation(
            summary = "Delete Card REST API",
            description = "Delete a card by ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Card not found",
                    content = @Content(
                            schema = @Schema(implementation = ResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ResponseDto.class)
                    )
            )
    })

    @DeleteMapping("/{cardId}")
    public ResponseEntity<ResponseDto> deleteCard(@PathVariable Long cardId) {
        boolean deleted = cardUseCase.deleteCard(cardId);
        ResponseDto responseDto = new ResponseDto();
        if (deleted) {
            responseDto.setStatusCode(CardConstants.STATUS_200);
            responseDto.setStatusMessage(CardConstants.MESSAGE_200);
            return ResponseEntity.ok(responseDto);
        } else {
            responseDto.setStatusCode(CardConstants.STATUS_500);
            responseDto.setStatusMessage(CardConstants.MESSAGE_500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    @Override
    public ResponseEntity<PagingCardResponseDto> getAllCards(Long ownerId, String title, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page != null ? page : 0, size != null ? size : 10);

        // If filtering parameters are provided, use filtered search
        if (ownerId != null || (title != null && !title.isEmpty())) {
            Page<CardResponseDto> filteredCards = cardUseCase.getFilteredCards(ownerId, title, pageable);
            return ResponseEntity.ok(PageUtils.toPagingCardResponseDto(filteredCards));
        }

        // Otherwise, get all cards
        Page<CardResponseDto> cards = cardUseCase.getAllCards(pageable);
        return ResponseEntity.ok(PageUtils.toPagingCardResponseDto(cards));
    }
}
