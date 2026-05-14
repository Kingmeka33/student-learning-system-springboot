package com.studentlearning.students;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<StudentEntity, String> {
  boolean existsByEmail(String email);
  List<StudentEntity> findByDeletedAtIsNullOrderByCreatedAtDesc();
  List<StudentEntity> findByDeletedAtIsNullAndNameContainingIgnoreCaseOrDeletedAtIsNullAndEmailContainingIgnoreCase(String name, String email);
  Optional<StudentEntity> findByEmail(String email);
}
