package com.studentlearning.assignments;

import com.studentlearning.courses.CourseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "assignment")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AssignmentEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private LocalDate dueDate;

  @ManyToOne
  @JoinColumn(name = "course_id", nullable = false)
  @JsonIgnore
  private CourseEntity course;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @PrePersist void onCreate() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }
  @PreUpdate void onUpdate() { updatedAt = LocalDateTime.now(); }
}
