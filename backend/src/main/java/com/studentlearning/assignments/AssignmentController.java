package com.studentlearning.assignments;
import com.studentlearning.common.security.SecurityRoleNames;

import com.studentlearning.assignments.AssignmentDtos.*;
import com.studentlearning.common.dto.DeleteResponse;
import com.studentlearning.common.dto.IdRequest;
import com.studentlearning.common.dto.SearchRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Assignments", description = "AssignmentEntity endpoints for the course-to-assignments one-to-many relationship.")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/assignments")
public class AssignmentController {
  private final AssignmentService assignmentService;
  public AssignmentController(AssignmentService assignmentService) { this.assignmentService = assignmentService; }

  @Operation(summary = "Get assignments", description = "Supports optional search by assignment title.")
  @GetMapping
  public ResponseEntity<List<AssignmentEntity>> findAll(@RequestParam(required = false) String search) {
    return ResponseEntity.ok(assignmentService.findAll(new SearchRequest(search)));
  }

  @Operation(summary = "Get one assignment by UUID")
  @GetMapping("/{id}")
  public ResponseEntity<AssignmentEntity> findOne(@PathVariable String id) {
    return ResponseEntity.ok(assignmentService.findOne(new IdRequest(id)));
  }

  @Operation(summary = "Create an assignment", description = "ADMIN only. The course must exist.")
  @PostMapping
  @PreAuthorize("hasRole('" + SecurityRoleNames.ADMIN_ROLE + "')")
  public ResponseEntity<AssignmentEntity> create(@Valid @RequestBody CreateAssignmentRequest request) {
    AssignmentEntity assignment = assignmentService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(assignment);
  }

  @Operation(summary = "Update an assignment", description = "ADMIN only.")
  @PatchMapping("/{id}")
  @PreAuthorize("hasRole('" + SecurityRoleNames.ADMIN_ROLE + "')")
  public ResponseEntity<AssignmentEntity> update(@PathVariable String id, @Valid @RequestBody UpdateAssignmentRequest request) {
    return ResponseEntity.ok(assignmentService.update(new IdRequest(id), request));
  }

  @Operation(summary = "Delete an assignment", description = "ADMIN only.")
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('" + SecurityRoleNames.ADMIN_ROLE + "')")
  public ResponseEntity<DeleteResponse> delete(@PathVariable String id) {
    assignmentService.delete(new IdRequest(id));
    return ResponseEntity.ok(new DeleteResponse("AssignmentEntity deleted successfully"));
  }
}
