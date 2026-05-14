package com.studentlearning.courses;
import com.studentlearning.common.security.SecurityRoleNames;

import com.studentlearning.common.dto.DeleteResponse;
import com.studentlearning.common.dto.IdRequest;
import com.studentlearning.common.dto.SearchRequest;
import com.studentlearning.courses.CourseDtos.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Courses", description = "CourseEntity CRUD and course relationship display endpoints.")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/courses")
public class CourseController {
  private final CourseService courseService;
  public CourseController(CourseService courseService) { this.courseService = courseService; }

  @Operation(summary = "Get courses", description = "ADMIN and STUDENT users can view courses. Supports optional search by title/code.")
  @GetMapping
  public ResponseEntity<List<CourseEntity>> findAll(@RequestParam(required = false) String search) {
    return ResponseEntity.ok(courseService.findAll(new SearchRequest(search)));
  }

  @Operation(summary = "Get one course by UUID")
  @GetMapping("/{id}")
  public ResponseEntity<CourseEntity> findOne(@PathVariable String id) {
    return ResponseEntity.ok(courseService.findOne(new IdRequest(id)));
  }

  @Operation(summary = "Create a course", description = "ADMIN only. CourseEntity code must be unique.")
  @PostMapping
  @PreAuthorize("hasRole('" + SecurityRoleNames.ADMIN_ROLE + "')")
  public ResponseEntity<CourseEntity> create(@Valid @RequestBody CreateCourseRequest request) {
    CourseEntity course = courseService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(course);
  }

  @Operation(summary = "Update a course", description = "ADMIN only.")
  @PatchMapping("/{id}")
  @PreAuthorize("hasRole('" + SecurityRoleNames.ADMIN_ROLE + "')")
  public ResponseEntity<CourseEntity> update(@PathVariable String id, @Valid @RequestBody UpdateCourseRequest request) {
    return ResponseEntity.ok(courseService.update(new IdRequest(id), request));
  }

  @Operation(summary = "Delete a course", description = "ADMIN only.")
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('" + SecurityRoleNames.ADMIN_ROLE + "')")
  public ResponseEntity<DeleteResponse> delete(@PathVariable String id) {
    courseService.delete(new IdRequest(id));
    return ResponseEntity.ok(new DeleteResponse("CourseEntity deleted successfully"));
  }
}
