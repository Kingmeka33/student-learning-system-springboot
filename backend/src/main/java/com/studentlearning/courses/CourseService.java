package com.studentlearning.courses;

import com.studentlearning.common.dto.IdRequest;
import com.studentlearning.common.dto.SearchRequest;
import com.studentlearning.common.exception.ApiException;
import com.studentlearning.courses.CourseDtos.CreateCourseRequest;
import com.studentlearning.courses.CourseDtos.UpdateCourseRequest;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CourseService {
  private final CourseRepository courseRepository;
  public CourseService(CourseRepository courseRepository) { this.courseRepository = courseRepository; }

  public CourseEntity create(CreateCourseRequest request) {
    try {
      if (courseRepository.existsByCode(request.code())) throw new ApiException(HttpStatus.CONFLICT, "Course code already exists");
      return courseRepository.save(CourseEntity.builder().title(request.title()).code(request.code()).build());
    } catch (ApiException ex) { throw ex; }
    catch (Exception ex) { throw new ApiException(HttpStatus.BAD_REQUEST, ex.getMessage()); }
  }

  public List<CourseEntity> findAll(SearchRequest request) {
    String search = request == null ? null : request.search();

    if (search == null || search.isBlank()) {
      return courseRepository.findByDeletedAtIsNullOrderByCreatedAtDesc();
    }

    return courseRepository.findByDeletedAtIsNullAndTitleContainingIgnoreCaseOrDeletedAtIsNullAndCodeContainingIgnoreCase(
        search,
        search
    );
  }

  public CourseEntity findOne(IdRequest request) {
    CourseEntity course = courseRepository.findById(request.id())
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Course not found"));

    if (course.getDeletedAt() != null) {
      throw new ApiException(HttpStatus.NOT_FOUND, "Course not found");
    }

    return course;
  }

  public CourseEntity update(IdRequest idRequest, UpdateCourseRequest request) {
    CourseEntity course = findOne(idRequest);
    if (request.title() != null && !request.title().isBlank()) course.setTitle(request.title());
    if (request.code() != null && !request.code().isBlank()) course.setCode(request.code());
    return courseRepository.save(course);
  }

  public void delete(IdRequest request) {
    CourseEntity course = findOne(request);
    course.setDeletedAt(java.time.LocalDateTime.now());
    courseRepository.save(course);
  }
}
