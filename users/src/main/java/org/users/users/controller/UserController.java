package org.users.users.controller;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.users.users.constants.UserConstants;
import org.users.users.dto.ErrorResponseDto;
import org.users.users.dto.ResponseDto;
import org.users.users.dto.UserDto;
import org.users.users.entity.User;
import org.users.users.service.IUserService;

@Tag(
        name = "CRUD REST API for Users",
        description = "CRUD REST API operations for Users microservice")
@RestController
@RequestMapping(path = "/api/v1", produces = {
        MediaType.APPLICATION_JSON_VALUE })
@Validated
public class UserController
{

    @Autowired
    public UserController(IUserService userService)
    {
        this.userService = userService;
    }

    private final IUserService userService;

    @Value("${build.version}")
    private String buildVersion;

    @ApiResponses({
        @ApiResponse(
                responseCode = "201",
                description = "HTTP Status CREATED"
        ),
        @ApiResponse(
                responseCode = "500",
                description = "HTTP Status INTERNAL_SERVER_ERROR",
                content = @Content(
                        schema = @Schema(implementation = ErrorResponseDto.class)
                )
        )}
    )
    @Operation(
            summary = "Create user REST API",
            description = "Create new User REST API")
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createUser(
            @Valid @RequestBody UserDto userDto)
    {
        User user = userService.createUser(userDto);
        if (user == null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseDto(UserConstants.STATUS_500,
                            UserConstants.MESSAGE_500));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(UserConstants.STATUS_201,
                        UserConstants.MESSAGE_201));
    }

    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "HTTP Status INTERNAL_SERVER_ERROR",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )
        )}
    )
    @Operation(
            summary = "Update User REST API",
            description = "Update User REST API")
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateUser(
            @Valid @RequestBody UserDto userDto)
    {
        boolean updated = userService.updateUser(userDto);
        if (updated)
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseDto(UserConstants.STATUS_200,
                            UserConstants.MESSAGE_200));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ResponseDto(UserConstants.STATUS_500,
                        UserConstants.MESSAGE_500));
    }

    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK"
    )
    @Operation(
            summary = "Fetch User by Id REST API",
            description = "Fetch User by Id REST API")
    @GetMapping("/fetch")
    public ResponseEntity<UserDto> fetchUser(
            @RequestParam Long id)
    {
        UserDto userDto = userService.fetchUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status INTERNAL_SERVER_ERROR",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @Operation(
            summary = "Delete User by Id REST API",
            description = "Delete User REST API")
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteUser(
            @RequestParam Long id)
    {
        boolean deleted = userService.deleteUser(id);
        if (deleted)
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseDto(UserConstants.STATUS_200,
                            UserConstants.MESSAGE_200));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ResponseDto(UserConstants.STATUS_500,
                        UserConstants.MESSAGE_500));
    }

    @Operation(
            summary = "Get Build Version",
            description = "Get the current build version of the service"
    )
    @GetMapping("/build-version")
    public ResponseEntity<String> getBuildVersion()
    {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buildVersion);
    }

}
