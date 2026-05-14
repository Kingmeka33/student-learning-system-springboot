package com.studentlearning.courses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studentlearning.assignments.AssignmentEntity;
import com.studentlearning.students.StudentEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Entity
@Table(name = "course")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CourseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false, unique = true)
  private String code;

  @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
  @Builder.Default
  private Set<AssignmentEntity> assignments = new HashSet<>();

  @ManyToMany(mappedBy = "courses")
  @JsonIgnoreProperties({"courses", "profile"})
  @Builder.Default
  private Set<StudentEntity> students = new HashSet<>();

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

  // Soft delete: null means active, timestamp means deleted.
  private LocalDateTime deletedAt;

  @PrePersist void onCreate() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }
  @PreUpdate void onUpdate() { updatedAt = LocalDateTime.now(); }
}
