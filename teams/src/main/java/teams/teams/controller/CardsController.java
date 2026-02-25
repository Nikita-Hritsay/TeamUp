package teams.teams.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import teams.teams.api.CardsApi;
import teams.teams.api.model.CardRequestDto;
import teams.teams.api.model.CardResponseDto;
import teams.teams.api.model.PagingCardResponseDto;
import teams.teams.api.model.ResponseDto;
import teams.teams.constants.CardConstants;
import teams.teams.service.ICardsService;
import teams.teams.util.PageUtils;

import java.util.List;

@Tag(
        name = "CRUD REST API for Cards",
        description = "Operations for managing project cards"
)
@RestController
@RequestMapping(path = "/api/v1/cards", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class CardsController implements CardsApi {

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
        cardsService.createCard(cardRequestDto);
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

    @Operation(
            operationId = "getCardsByTeamId",
            summary = "Get Cards by team ID REST API",
            description = "Get all cards for a team by team ID",
            tags = { "Cards" },
            responses = {
                    @ApiResponse(responseCode = "200", description = "HTTP Status OK", content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CardResponseDto.class)))
                    }),
                    @ApiResponse(responseCode = "400", description = "Bad Request - teamId is required")
            }
    )
    @GetMapping("/fetchByTeam")
    public ResponseEntity<List<CardResponseDto>> getCardsByTeamId(@RequestParam Long teamId) {
        if (teamId == null) {
            return ResponseEntity.badRequest().build();
        }
        List<CardResponseDto> cards = cardsService.getCardsByTeamId(teamId);
        return ResponseEntity.ok(cards);
    }

    @Override
    public ResponseEntity<PagingCardResponseDto> getAllCards(Long ownerId, String title, Long teamId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page != null ? page : 0, size != null ? size : 10);

        // If filtering parameters are provided, use filtered search
        if (ownerId != null || (title != null && !title.isEmpty()) || teamId != null) {
            Page<CardResponseDto> filteredCards = cardsService.getFilteredCards(ownerId, title, teamId, pageable);
            return ResponseEntity.ok(PageUtils.toPagingCardResponseDto(filteredCards));
        }

        // Otherwise, get all cards
        Page<CardResponseDto> cards = cardsService.getAllCards(pageable);
        return ResponseEntity.ok(PageUtils.toPagingCardResponseDto(cards));
    }
}
