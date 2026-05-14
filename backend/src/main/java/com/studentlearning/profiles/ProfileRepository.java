package com.studentlearning.profiles;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<ProfileEntity, String> {
  boolean existsByStudentId(String studentId);
  List<ProfileEntity> findByBioContainingIgnoreCase(String bio);
}
