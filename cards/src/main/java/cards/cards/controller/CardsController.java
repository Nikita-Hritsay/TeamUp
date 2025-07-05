package cards.cards.controller;

import cards.cards.constants.CardConstants;
import cards.cards.dto.CardRequestDto;
import cards.cards.dto.CardResponseDto;
import cards.cards.dto.ResponseDto;
import cards.cards.service.ICardsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "CRUD REST API for Cards",
        description = "Operations for managing project cards"
)
@RestController
@RequestMapping(path = "/api/v1", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class CardsController {

    private final ICardsService cardsService;

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    public CardsController(ICardsService cardsService) {
        this.cardsService = cardsService;
    }

    @Operation(
            summary = "Get Build Version",
            description = "Get the current build version of the service"
    )
    @GetMapping("/build-version")
    public ResponseEntity<String> getBuildVersion() {
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
        cardsService.createCard(cardRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(CardConstants.STATUS_201, CardConstants.MESSAGE_201));
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
        CardResponseDto cardResponseDto = cardsService.getCardById(cardId);
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
        List<CardResponseDto> cardResponseDto = cardsService.getCardsByUserId(userId);
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
    public ResponseEntity<Page<CardResponseDto>> getAllCards(
            @RequestParam(required = false) Long ownerId,
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);

        // If filtering parameters are provided, use filtered search
        if (ownerId != null || (title != null && !title.isEmpty())) {
            Page<CardResponseDto> filteredCards = cardsService.getFilteredCards(ownerId, title, pageable);
            return ResponseEntity.ok(filteredCards);
        }

        // Otherwise, get all cards
        Page<CardResponseDto> cards = cardsService.getAllCards(pageable);
        return ResponseEntity.ok(cards);
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
        CardResponseDto updatedCard = cardsService.updateCard(cardId, cardRequestDto);
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
        boolean deleted = cardsService.deleteCard(cardId);
        if (deleted) {
            return ResponseEntity.ok(
                    new ResponseDto(CardConstants.STATUS_200, CardConstants.MESSAGE_200));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseDto(CardConstants.STATUS_500, CardConstants.MESSAGE_500));
        }
    }
}
