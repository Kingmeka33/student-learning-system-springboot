package com.studentlearning.enrollments;

import com.studentlearning.auth.AppUserEntity;
import com.studentlearning.common.security.SecurityRoleNames;
import com.studentlearning.enrollments.EnrollmentDtos.*;
import com.studentlearning.students.StudentEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Enrollments", description = "Many-to-many student/course enrollment endpoints.")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {
  private final EnrollmentService enrollmentService;

  public EnrollmentController(EnrollmentService enrollmentService) {
    this.enrollmentService = enrollmentService;
  }

  @Operation(summary = "View enrollments", description = "Returns students with their enrolled courses.")
  @GetMapping
  public ResponseEntity<List<StudentEntity>> findAll() {
    return ResponseEntity.ok(enrollmentService.findAll());
  }

  @Operation(summary = "Enroll one student in one course", description = "ADMIN only. Prevents duplicate enrollments.")
  @PostMapping
  @PreAuthorize("hasRole('" + SecurityRoleNames.ADMIN_ROLE + "')")
  public ResponseEntity<StudentEntity> enroll(@Valid @RequestBody EnrollRequest request) {
    StudentEntity student = enrollmentService.enroll(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(student);
  }

  @Operation(summary = "Enroll one student into multiple courses", description = "ADMIN only. Supports frontend multi-selector.")
  @PostMapping("/multi")
  @PreAuthorize("hasRole('" + SecurityRoleNames.ADMIN_ROLE + "')")
  public ResponseEntity<StudentEntity> multiEnroll(@Valid @RequestBody MultiEnrollRequest request) {
    StudentEntity student = enrollmentService.multiEnroll(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(student);
  }

  @Operation(summary = "Enroll logged-in student", description = "STUDENT only. A student can enroll only themselves into selected courses.")
  @PostMapping("/me")
  @PreAuthorize("hasRole('" + SecurityRoleNames.STUDENT_ROLE + "')")
  public ResponseEntity<StudentEntity> enrollMe(Authentication authentication, @Valid @RequestBody MultiEnrollRequest request) {
    AppUserEntity currentUser = (AppUserEntity) authentication.getPrincipal();
    StudentEntity student = enrollmentService.enrollLoggedInStudent(currentUser.getEmail(), request.courseIds());
    return ResponseEntity.status(HttpStatus.CREATED).body(student);
  }
}
