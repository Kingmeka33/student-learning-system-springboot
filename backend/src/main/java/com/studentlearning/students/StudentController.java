package com.studentlearning.students;

import com.studentlearning.common.dto.DeleteResponse;
import com.studentlearning.common.dto.IdRequest;
import com.studentlearning.common.dto.SearchRequest;
import com.studentlearning.common.security.SecurityRoleNames;
import com.studentlearning.students.StudentDtos.CreateStudentRequest;
import com.studentlearning.students.StudentDtos.UpdateStudentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Students", description = "StudentEntity CRUD, search and soft delete endpoints.")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/students")
public class StudentController {
  private final StudentService studentService;

  public StudentController(StudentService studentService) { this.studentService = studentService; }

  @Operation(summary = "Get students", description = "Returns active students only and supports optional search by name/email.")
  @GetMapping
  public ResponseEntity<List<StudentEntity>> findAll(@RequestParam(required = false) String search) {
    return ResponseEntity.ok(studentService.findAll(new SearchRequest(search)));
  }

  @Operation(summary = "Get one student by UUID")
  @GetMapping("/{id}")
  public ResponseEntity<StudentEntity> findOne(@PathVariable String id) {
    return ResponseEntity.ok(studentService.findOne(new IdRequest(id)));
  }

  @Operation(summary = "Create a student", description = "ADMIN only. Email must be unique.")
  @PostMapping
  @PreAuthorize("hasRole('" + SecurityRoleNames.ADMIN_ROLE + "')")
  public ResponseEntity<StudentEntity> create(@Valid @RequestBody CreateStudentRequest request) {
    StudentEntity student = studentService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(student);
  }

  @Operation(summary = "Update a student", description = "ADMIN only.")
  @PatchMapping("/{id}")
  @PreAuthorize("hasRole('" + SecurityRoleNames.ADMIN_ROLE + "')")
  public ResponseEntity<StudentEntity> update(@PathVariable String id, @Valid @RequestBody UpdateStudentRequest request) {
    return ResponseEntity.ok(studentService.update(new IdRequest(id), request));
  }

  @Operation(summary = "Soft delete a student", description = "ADMIN only. Sets deletedAt instead of permanently deleting.")
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('" + SecurityRoleNames.ADMIN_ROLE + "')")
  public ResponseEntity<DeleteResponse> remove(@PathVariable String id) {
    studentService.softDelete(new IdRequest(id));
    return ResponseEntity.ok(new DeleteResponse("StudentEntity soft deleted successfully"));
  }
}
