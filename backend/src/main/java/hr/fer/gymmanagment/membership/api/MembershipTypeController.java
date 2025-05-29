package hr.fer.gymmanagment.membership.api;

import hr.fer.gymmanagment.common.ApiErrorResponse;
import hr.fer.gymmanagment.common.NotFoundException;
import hr.fer.gymmanagment.membership.dto.MembershipTypeDto;
import hr.fer.gymmanagment.membership.service.MembershipTypeService;
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

import static hr.fer.gymmanagment.common.Constants.Api.V1;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(V1 + "/membership-types")
@Tag(name = "Membership Types", description = "Membership Types operations")
public class MembershipTypeController {
    private final MembershipTypeService membershipTypeService;

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = MembershipTypeDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @Operation(summary = "Create new membership type")
    public ResponseEntity<MembershipTypeDto> createMembershipType(@Valid @RequestBody MembershipTypeDto membershipTypeDto) {
        log.info("Creating new membership type");
        MembershipTypeDto created = membershipTypeService.createMembershipType(membershipTypeDto);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MembershipTypeDto.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @Operation(summary = "Get all membership types")
    public ResponseEntity<List<MembershipTypeDto>> getAllMembershipTypes() {
        log.info("Fetching all membership types");
        return ResponseEntity.ok(membershipTypeService.getAllMembershipTypes());
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = MembershipTypeDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @Operation(summary = "Get membership type by ID")
    public ResponseEntity<MembershipTypeDto> getMembershipTypeById(@PathVariable Integer id) {
        log.info("Fetching membership type with id: {}", id);
        return membershipTypeService.getMembershipTypeById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("Membership type not found with id: " + id));
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = MembershipTypeDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @Operation(summary = "Update membership type")
    public ResponseEntity<MembershipTypeDto> updateMembershipType(@PathVariable Integer id,
                                                                  @Valid @RequestBody MembershipTypeDto membershipTypeDto) {
        log.info("Updating membership type with id: {}", id);
        return ResponseEntity.ok(membershipTypeService.updateMembershipType(id, membershipTypeDto));
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @Operation(summary = "Delete membership type")
    public ResponseEntity<Void> deleteMembershipType(@PathVariable Integer id) {
        log.info("Deleting membership type with id: {}", id);
        membershipTypeService.deleteMembershipType(id);
        return ResponseEntity.noContent().build();
    }
}
