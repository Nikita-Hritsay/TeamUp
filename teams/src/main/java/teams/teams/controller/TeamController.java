package teams.teams.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import teams.teams.constants.TeamConstants;
import teams.teams.dto.TeamsResponseDto;
import teams.teams.dto.TeamMemberRequestDto;
import teams.teams.dto.TeamMemberResponseDto;
import teams.teams.service.ITeamService;

import java.util.List;

@Tag(
        name = "CRUD REST API for Team Management",
        description = "Operations for managing team members in projects"
)
@RestController
@RequestMapping(path = "/api/v1", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class TeamController {

    private final ITeamService teamService;
    
    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    public TeamController(ITeamService teamService) {
        this.teamService = teamService;
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
            summary = "Join Team REST API",
            description = "Submit a request to join a team/project"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status CREATED"
    )
    @PostMapping("/{cardId}/join")
    public ResponseEntity<TeamsResponseDto> joinTeam(
            @PathVariable Long cardId,
            @Valid @RequestBody TeamMemberRequestDto teamMemberRequestDto) {
        teamService.joinTeam(cardId, teamMemberRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new TeamsResponseDto(TeamConstants.STATUS_201, TeamConstants.MESSAGE_201));
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
    public ResponseEntity<TeamsResponseDto> inviteToTeam(
            @PathVariable Long cardId,
            @Valid @RequestBody TeamMemberRequestDto teamMemberRequestDto) {
        teamService.inviteToTeam(cardId, teamMemberRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new TeamsResponseDto(TeamConstants.STATUS_201, TeamConstants.MESSAGE_201));
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
                            schema = @Schema(implementation = TeamsResponseDto.class)
                    )
            )
    })
    @PutMapping("/{cardId}/status")
    public ResponseEntity<TeamMemberResponseDto> updateMemberStatus(
            @PathVariable Long cardId,
            @RequestParam Long userId,
            @RequestParam String status) {
        TeamMemberResponseDto updatedMember = teamService.updateMemberStatus(cardId, userId, status);
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
                            schema = @Schema(implementation = TeamsResponseDto.class)
                    )
            )
    })
    @DeleteMapping("/{cardId}/remove")
    public ResponseEntity<TeamsResponseDto> removeMember(
            @PathVariable Long cardId,
            @RequestParam Long userId) {
        boolean removed = teamService.removeMember(cardId, userId);
        if (removed) {
            return ResponseEntity.ok(
                    new TeamsResponseDto(TeamConstants.STATUS_200, TeamConstants.MESSAGE_200));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new TeamsResponseDto(TeamConstants.STATUS_500, TeamConstants.MESSAGE_500));
        }
    }

    @Operation(
            summary = "Get Team Members REST API",
            description = "Get all members of a specific team/project"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK"
    )
    @GetMapping("/{cardId}")
    public ResponseEntity<List<TeamMemberResponseDto>> getTeamMembers(@PathVariable Long cardId) {
        List<TeamMemberResponseDto> teamMembers = teamService.getTeamMembers(cardId);
        return ResponseEntity.ok(teamMembers);
    }
}