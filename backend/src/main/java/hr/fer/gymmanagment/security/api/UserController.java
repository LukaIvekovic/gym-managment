package hr.fer.gymmanagment.security.api;

import hr.fer.gymmanagment.GymManagmentException;
import hr.fer.gymmanagment.security.entity.dto.request.LoginRequest;
import hr.fer.gymmanagment.security.entity.dto.request.SignupRequest;
import hr.fer.gymmanagment.security.entity.dto.response.JwtResponse;
import hr.fer.gymmanagment.security.entity.dto.response.SignupResponse;
import hr.fer.gymmanagment.security.entity.dto.response.UserResponse;
import hr.fer.gymmanagment.security.entity.pojo.RoleEnum;
import hr.fer.gymmanagment.security.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static hr.fer.gymmanagment.common.Constants.Api.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = V1 + USERS)
@Tag(name = "Users", description = "Users operations")
public class UserController {

    private final UserService userService;

    @PostMapping(LOGIN)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @Operation(summary = "Fetches access token / authenticates user")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Authenticating user");

        return ResponseEntity.ok(userService.authenticateUser(loginRequest));
    }

    @PostMapping(REGISTRATION)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SignupResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @Operation(summary = "Register user")
    public ResponseEntity<SignupResponse> addUser(@Valid @RequestBody SignupRequest signupRequest) {
        log.info("Adding user");

        if (userService.userExistsByEmail(signupRequest.getEmail())) {
            log.info("Adding user failed cause this email already exists in the database");
            throw new GymManagmentException("Korisnik s unesenim e-mailom već postoji");
        } else {
            Integer userId = userService.addUser(signupRequest);
            log.info("Added user with id {}", userId);
            return ResponseEntity.status(201).body(new SignupResponse(userId));
        }
    }

    @GetMapping("/trainers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<List<UserResponse>> getTrainers() {
        log.info("Fetching trainers");
        return ResponseEntity.ok(userService.getUsersByRole(RoleEnum.TRENER));
    }

    @GetMapping("/gym-goers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<List<UserResponse>> getGymGoers() {
        log.info("Fetching gym goers");
        return ResponseEntity.ok(userService.getUsersByRole(RoleEnum.KORISNIK));
    }
}