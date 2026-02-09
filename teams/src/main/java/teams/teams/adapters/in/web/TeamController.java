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
import teams.teams.api.TeamsApi;
import teams.teams.api.model.*;
import teams.teams.application.port.in.TeamUseCase;
import teams.teams.constants.TeamConstants;
import teams.teams.util.PageUtils;

/**
 * REST controller adapter for Team operations.
 * This is a thin adapter that delegates to the application layer.
 */
@Tag(
        name = "CRUD REST API for Team Management",
        description = "Operations for managing team members in projects"
)
@RestController
@RequestMapping(path = "/api/v1/teams", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@RequiredArgsConstructor
public class TeamController implements TeamsApi {

    private final TeamUseCase teamUseCase;

    @Value("${build.version}")
    private String buildVersion;

    @Operation(
            summary = "Get Build Version",
            description = "Get the current build version of the service"
    )
    @GetMapping("/build-version")
    public ResponseEntity<String> getTeamsBuildVersion() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buildVersion);
    }

    @Operation(
            summary = "Create Team REST API",
            description = "Submit a request to create a team/project"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status CREATED"
    )
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createTeam(
            @Valid @RequestBody TeamRequestDto teamRequestDto) {
        teamUseCase.createTeam(teamRequestDto);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatusCode(TeamConstants.STATUS_201);
        responseDto.setStatusMessage(TeamConstants.MESSAGE_201_TEAM_CREATED);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDto);
    }

    @Operation(
            summary = "Join Team REST API",
            description = "Submit a request to join a team/project"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status CREATED"
    )
    @PostMapping("/join")
    public ResponseEntity<ResponseDto> joinTeam(
            @Valid @RequestBody TeamMemberRequestDto teamMemberRequestDto) {
        teamUseCase.joinTeam(teamMemberRequestDto);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatusCode(TeamConstants.STATUS_201);
        responseDto.setStatusMessage(TeamConstants.MESSAGE_201);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDto);
    }

    @Operation(
            summary = "Invite to Team REST API",
            description = "Invite a user to join a team/project"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status CREATED"
    )
    @PostMapping("/{cardId}/invite")
    public ResponseEntity<ResponseDto> inviteToTeam(
            @PathVariable Long cardId,
            @Valid @RequestBody TeamMemberRequestDto teamMemberRequestDto) {
        teamUseCase.inviteToTeam(cardId, teamMemberRequestDto);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatusCode(TeamConstants.STATUS_201);
        responseDto.setStatusMessage(TeamConstants.MESSAGE_201);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDto);
    }

    @Operation(
            summary = "Update Member Status REST API",
            description = "Update the status of a team member (accept/reject)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Team member not found",
                    content = @Content(
                            schema = @Schema(implementation = ResponseDto.class)
                    )
            )
    })
    @PutMapping("/{cardId}/status")
    public ResponseEntity<TeamMemberResponseDto> updateMemberStatus(
            @PathVariable Long cardId,
            @RequestParam Long userId,
            @RequestParam String status) {
        TeamMemberResponseDto updatedMember = teamUseCase.updateMemberStatus(cardId, userId, status);
        return ResponseEntity.ok(updatedMember);
    }

    @Operation(
            summary = "Remove Team Member REST API",
            description = "Remove a user from a team/project"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Team member not found",
                    content = @Content(
                            schema = @Schema(implementation = ResponseDto.class)
                    )
            )
    })
    @DeleteMapping("/{cardId}/remove")
    public ResponseEntity<ResponseDto> removeMember(
            @PathVariable Long cardId,
            @RequestParam Long userId) {
        boolean removed = teamUseCase.removeMember(cardId, userId);
        ResponseDto responseDto = new ResponseDto();
        if (removed) {
            responseDto.setStatusCode(TeamConstants.STATUS_200);
            responseDto.setStatusMessage(TeamConstants.MESSAGE_200);
            return ResponseEntity.ok(responseDto);
        } else {
            responseDto.setStatusCode(TeamConstants.STATUS_500);
            responseDto.setStatusMessage(TeamConstants.MESSAGE_500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    @Operation(
            summary = "Get Team Members REST API",
            description = "Get all members of a specific team/project with pagination"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK"
    )
    @GetMapping("/{cardId}")
    public ResponseEntity<PagingTeamMemberResponseDto> getTeamMembers(
            @PathVariable Long cardId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TeamMemberResponseDto> teamMembers = teamUseCase.getTeamMembers(cardId, pageable);
        return ResponseEntity.ok(PageUtils.toPagingTeamMemberResponseDto(teamMembers));
    }

    @Operation(
            summary = "Get Team Info REST API",
            description = "Get info of a specific team"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK"
    )
    @GetMapping("/fetch")
    public ResponseEntity<TeamResponseDto> fetch(@RequestParam Long teamId) {
        TeamResponseDto teamResponseDto = teamUseCase.fetchTeam(teamId);
        return ResponseEntity.ok(teamResponseDto);
    }

    @Override
    public ResponseEntity<PagingTeamMemberResponseDto> getTeamMembers(Long cardId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page != null ? page : 0, size != null ? size : 10);
        Page<TeamMemberResponseDto> teamMembers = teamUseCase.getTeamMembers(cardId, pageable);
        return ResponseEntity.ok(PageUtils.toPagingTeamMemberResponseDto(teamMembers));
    }
}
