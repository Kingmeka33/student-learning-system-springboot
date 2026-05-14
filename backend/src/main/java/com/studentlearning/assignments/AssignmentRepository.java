package com.studentlearning.assignments;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<AssignmentEntity, String> {
  List<AssignmentEntity> findByTitleContainingIgnoreCase(String title);
}
