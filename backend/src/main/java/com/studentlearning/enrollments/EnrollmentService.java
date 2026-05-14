package com.studentlearning.enrollments;

import com.studentlearning.common.dto.IdRequest;
import com.studentlearning.common.exception.ApiException;
import com.studentlearning.courses.CourseEntity;
import com.studentlearning.courses.CourseService;
import com.studentlearning.enrollments.EnrollmentDtos.*;
import com.studentlearning.students.StudentEntity;
import com.studentlearning.students.StudentRepository;
import com.studentlearning.students.StudentService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentService {
  private final StudentService studentService;
  private final CourseService courseService;
  private final StudentRepository studentRepository;

  public EnrollmentService(StudentService studentService, CourseService courseService, StudentRepository studentRepository) {
    this.studentService = studentService;
    this.courseService = courseService;
    this.studentRepository = studentRepository;
  }

  public StudentEntity enroll(EnrollRequest request) {
    StudentEntity student = studentService.findOne(new IdRequest(request.studentId()));
    CourseEntity course = courseService.findOne(new IdRequest(request.courseId()));
    boolean alreadyEnrolled = student.getCourses().stream().anyMatch(item -> item.getId().equals(course.getId()));
    if (alreadyEnrolled) throw new ApiException(HttpStatus.CONFLICT, "StudentEntity is already enrolled in this course");
    student.getCourses().add(course);
    return studentRepository.save(student);
  }

  public StudentEntity multiEnroll(MultiEnrollRequest request) {
    StudentEntity student = studentService.findOne(new IdRequest(request.studentId()));

    int newlyAddedCount = addNewCoursesToStudent(student, request.courseIds());

    if (newlyAddedCount == 0) {
      throw new ApiException(HttpStatus.CONFLICT, "StudentEntity is already enrolled in all selected courses");
    }

    return studentRepository.save(student);
  }

  public StudentEntity enrollLoggedInStudent(String email, List<String> courseIds) {
    StudentEntity student = studentRepository.findByEmail(email)
        .orElseThrow(() -> new ApiException(
            HttpStatus.NOT_FOUND,
            "Logged-in student record was not found. Please contact an admin to link your account to a student record."
        ));

    int newlyAddedCount = addNewCoursesToStudent(student, courseIds);

    if (newlyAddedCount == 0) {
      throw new ApiException(HttpStatus.CONFLICT, "You are already enrolled in all selected courses");
    }

    return studentRepository.save(student);
  }

  private int addNewCoursesToStudent(StudentEntity student, List<String> courseIds) {
    int newlyAddedCount = 0;

    for (String courseId : courseIds) {
      CourseEntity course = courseService.findOne(new IdRequest(courseId));

      boolean alreadyEnrolled = student.getCourses().stream()
          .anyMatch(existingCourse -> existingCourse.getId().equals(course.getId()));

      if (!alreadyEnrolled) {
        student.getCourses().add(course);
        newlyAddedCount++;
      }
    }

    return newlyAddedCount;
  }

  public List<StudentEntity> findAll() {
    return studentService.findAll(null);
  }
}
