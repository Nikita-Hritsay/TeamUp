package org.users.users.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.users.users.constants.UserConstants;
import org.users.users.dto.UserDto;
import org.users.users.dto.ErrorResponseDto;
import org.users.users.dto.ResponseDto;
import org.users.users.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
    name = "CRUD REST API for Users",
    description = "CRUD REST API operations for Users microservice")
@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class UserController
{

    private IUserService userService;

    @ApiResponse(
     responseCode = "201",
     description = "HTTP Status CREATED"
    )
    @Operation(
        summary = "Create user REST API",
        description = "Create new User REST API")
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createUser(@Valid @RequestBody UserDto userDto) {
        userService.createUser(userDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(UserConstants.STATUS_201, UserConstants.MESSAGE_201));
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
            summary = "Update User REST API",
            description = "Update new User REST API")
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateUser(@Valid @RequestBody UserDto userDto) {
        boolean updated = userService.updateUser(userDto);
        if (updated)
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseDto(UserConstants.STATUS_200, UserConstants.MESSAGE_200));
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseDto(UserConstants.STATUS_500, UserConstants.MESSAGE_500));
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
            @RequestParam Long id) {
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
            @RequestParam Long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted)
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseDto(UserConstants.STATUS_200, UserConstants.MESSAGE_200));
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseDto(UserConstants.STATUS_500, UserConstants.MESSAGE_500));
    }

}
