package com.studentlearning.courses;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<CourseEntity, String> {
  boolean existsByCode(String code);
  List<CourseEntity> findByTitleContainingIgnoreCaseOrCodeContainingIgnoreCase(String title, String code);
  List<CourseEntity> findByDeletedAtIsNullOrderByCreatedAtDesc();
  List<CourseEntity> findByDeletedAtIsNullAndTitleContainingIgnoreCaseOrDeletedAtIsNullAndCodeContainingIgnoreCase(String title, String code);
}
