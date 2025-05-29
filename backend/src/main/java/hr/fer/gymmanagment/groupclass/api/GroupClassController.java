package hr.fer.gymmanagment.groupclass.api;

import hr.fer.gymmanagment.common.ApiErrorResponse;
import hr.fer.gymmanagment.common.NotFoundException;
import hr.fer.gymmanagment.groupclass.dto.GroupClassDto;
import hr.fer.gymmanagment.groupclass.service.GroupClassService;
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
@RequestMapping(V1 + "/group-classes")
@Tag(name = "Group Classes", description = "Group Classes operations")
public class GroupClassController {
    private final GroupClassService groupClassService;

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = GroupClassDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @Operation(summary = "Create new group class")
    public ResponseEntity<GroupClassDto> createGroupClass(@Valid @RequestBody GroupClassDto groupClassDto) {
        log.info("Creating new group class");
        GroupClassDto created = groupClassService.createGroupClass(groupClassDto);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = GroupClassDto.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @Operation(summary = "Get all group classes")
    public ResponseEntity<List<GroupClassDto>> getAllGroupClasses() {
        log.info("Fetching all group classes");
        return ResponseEntity.ok(groupClassService.getAllGroupClasses());
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = GroupClassDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @Operation(summary = "Get group class by ID")
    public ResponseEntity<GroupClassDto> getGroupClassById(@PathVariable Integer id) {
        log.info("Fetching group class with id: {}", id);
        return groupClassService.getGroupClassById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("Group class not found with id: " + id));
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = GroupClassDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @Operation(summary = "Update group class")
    public ResponseEntity<GroupClassDto> updateGroupClass(@PathVariable Integer id,
                                                          @Valid @RequestBody GroupClassDto groupClassDto) {
        log.info("Updating group class with id: {}", id);
        return ResponseEntity.ok(groupClassService.updateGroupClass(id, groupClassDto));
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @Operation(summary = "Delete group class")
    public ResponseEntity<Void> deleteGroupClass(@PathVariable Integer id) {
        log.info("Deleting group class with id: {}", id);
        groupClassService.deleteGroupClass(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/participants")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = GroupClassDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @Operation(summary = "Add participant to group class")
    public ResponseEntity<GroupClassDto> addParticipant(@PathVariable Integer id,
                                                        @AuthenticationPrincipal User user) {
        log.info("Adding participant to group class with id: {}", id);
        return ResponseEntity.ok(groupClassService.addParticipant(id, user));
    }

    @DeleteMapping("/{id}/participants")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = GroupClassDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @Operation(summary = "Remove participant from group class")
    public ResponseEntity<GroupClassDto> removeParticipant(@PathVariable Integer id,
                                                           @AuthenticationPrincipal User user) {
        log.info("Removing participant from group class with id: {}", id);
        return ResponseEntity.ok(groupClassService.removeParticipant(id, user));
    }
}
