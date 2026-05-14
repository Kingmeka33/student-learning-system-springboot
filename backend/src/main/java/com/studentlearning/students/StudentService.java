package com.studentlearning.students;

import com.studentlearning.common.dto.IdRequest;
import com.studentlearning.common.dto.SearchRequest;
import com.studentlearning.common.exception.ApiException;
import com.studentlearning.students.StudentDtos.CreateStudentRequest;
import com.studentlearning.students.StudentDtos.UpdateStudentRequest;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
  private final StudentRepository studentRepository;

  public StudentService(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  public StudentEntity create(CreateStudentRequest request) {
    try {
      if (studentRepository.existsByEmail(request.email())) {
        throw new ApiException(HttpStatus.CONFLICT, "Duplicate student email already exists");
      }
      StudentEntity student = StudentEntity.builder().name(request.name()).email(request.email()).build();
      return studentRepository.save(student);
    } catch (ApiException ex) { throw ex; }
    catch (Exception ex) { throw new ApiException(HttpStatus.BAD_REQUEST, ex.getMessage()); }
  }

  public List<StudentEntity> findAll(SearchRequest request) {
    String search = request == null ? null : request.search();
    if (search != null && !search.isBlank()) {
      return studentRepository.findByDeletedAtIsNullAndNameContainingIgnoreCaseOrDeletedAtIsNullAndEmailContainingIgnoreCase(search, search);
    }
    return studentRepository.findByDeletedAtIsNullOrderByCreatedAtDesc();
  }

  public StudentEntity findOne(IdRequest request) {
    return studentRepository.findById(request.id())
        .filter(student -> student.getDeletedAt() == null)
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "StudentEntity not found"));
  }

  public StudentEntity update(IdRequest idRequest, UpdateStudentRequest request) {
    try {
      StudentEntity student = findOne(idRequest);
      if (request.name() != null && !request.name().isBlank()) student.setName(request.name());
      if (request.email() != null && !request.email().isBlank()) student.setEmail(request.email());
      return studentRepository.save(student);
    } catch (ApiException ex) { throw ex; }
    catch (Exception ex) { throw new ApiException(HttpStatus.BAD_REQUEST, ex.getMessage()); }
  }

  public void softDelete(IdRequest request) {
    StudentEntity student = findOne(request);
    student.setDeletedAt(LocalDateTime.now());
    studentRepository.save(student);
  }
}
