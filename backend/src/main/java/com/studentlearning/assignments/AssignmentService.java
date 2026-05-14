package com.studentlearning.assignments;

import com.studentlearning.assignments.AssignmentDtos.*;
import com.studentlearning.common.dto.IdRequest;
import com.studentlearning.common.dto.SearchRequest;
import com.studentlearning.common.exception.ApiException;
import com.studentlearning.courses.CourseEntity;
import com.studentlearning.courses.CourseService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AssignmentService {
  private final AssignmentRepository assignmentRepository;
  private final CourseService courseService;
  public AssignmentService(AssignmentRepository assignmentRepository, CourseService courseService) {
    this.assignmentRepository = assignmentRepository;
    this.courseService = courseService;
  }
  public AssignmentEntity create(CreateAssignmentRequest request) {
    try {
      CourseEntity course = courseService.findOne(new IdRequest(request.courseId()));
      return assignmentRepository.save(AssignmentEntity.builder().title(request.title()).dueDate(request.dueDate()).course(course).build());
    } catch (ApiException ex) { throw ex; }
    catch (Exception ex) { throw new ApiException(HttpStatus.BAD_REQUEST, ex.getMessage()); }
  }
  public List<AssignmentEntity> findAll(SearchRequest request) {
    String search = request == null ? null : request.search();
    if (search != null && !search.isBlank()) return assignmentRepository.findByTitleContainingIgnoreCase(search);
    return assignmentRepository.findAll();
  }
  public AssignmentEntity findOne(IdRequest request) { return assignmentRepository.findById(request.id()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "AssignmentEntity not found")); }
  public AssignmentEntity update(IdRequest idRequest, UpdateAssignmentRequest request) {
    AssignmentEntity assignment = findOne(idRequest);
    if (request.title() != null && !request.title().isBlank()) assignment.setTitle(request.title());
    if (request.dueDate() != null) assignment.setDueDate(request.dueDate());
    if (request.courseId() != null && !request.courseId().isBlank()) assignment.setCourse(courseService.findOne(new IdRequest(request.courseId())));
    return assignmentRepository.save(assignment);
  }
  public void delete(IdRequest request) { assignmentRepository.delete(findOne(request)); }
}
