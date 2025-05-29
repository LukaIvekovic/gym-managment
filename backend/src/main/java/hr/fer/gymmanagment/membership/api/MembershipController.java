package hr.fer.gymmanagment.membership.api;

import hr.fer.gymmanagment.common.ApiErrorResponse;
import hr.fer.gymmanagment.common.NotFoundException;
import hr.fer.gymmanagment.membership.dto.MembershipDto;
import hr.fer.gymmanagment.membership.service.MembershipService;
import hr.fer.gymmanagment.security.entity.User;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static hr.fer.gymmanagment.common.Constants.Api.V1;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(V1 + "/memberships")
@Tag(name = "Memberships", description = "Memberships operations")
public class MembershipController {
    private final MembershipService membershipService;

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = MembershipDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @Operation(summary = "Create new membership")
    public ResponseEntity<MembershipDto> createMembership(@AuthenticationPrincipal User user,
                                                          @Valid @RequestBody MembershipDto membershipDto) {
        log.info("Creating new membership for user: {} with type: {}", user.getId(), membershipDto.typeId());
        MembershipDto created = membershipService.createMembership(user, membershipDto);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MembershipDto.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @Operation(summary = "Get all memberships")
    public ResponseEntity<List<MembershipDto>> getAllMemberships() {
        log.info("Fetching all memberships");
        return ResponseEntity.ok(membershipService.getAllMemberships());
    }

    @GetMapping("/user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MembershipDto.class)))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @Operation(summary = "Get all memberships for a user")
    public ResponseEntity<List<MembershipDto>> getAllUserMemberships(@AuthenticationPrincipal User user) {
        log.info("Fetching all memberships for user: {}", user.getId());
        return ResponseEntity.ok(membershipService.getAllUserMemberships(user));
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = MembershipDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @Operation(summary = "Get membership by ID")
    public ResponseEntity<MembershipDto> getMembershipById(@PathVariable Integer id) {
        log.info("Fetching membership with id: {}", id);
        return membershipService.getMembershipById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("Membership not found with id: " + id));
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = MembershipDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @Operation(summary = "Update membership")
    public ResponseEntity<MembershipDto> updateMembership(@PathVariable Integer id,
                                                          @Valid @RequestBody MembershipDto membershipDto,
                                                          @AuthenticationPrincipal User user) {
        log.info("Updating membership with id: {} for user: {} with type: {}", id, user.getId(), membershipDto.typeId());
        return ResponseEntity.ok(membershipService.updateMembership(id, user, membershipDto));
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @Operation(summary = "Delete membership")
    public ResponseEntity<Void> deleteMembership(@PathVariable Integer id) {
        log.info("Deleting membership with id: {}", id);
        membershipService.deleteMembership(id);
        return ResponseEntity.noContent().build();
    }
}
