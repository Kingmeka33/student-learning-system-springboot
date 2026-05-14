package com.studentlearning.students;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studentlearning.courses.CourseEntity;
import com.studentlearning.profiles.ProfileEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Entity
@Table(name = "student")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StudentEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
  private ProfileEntity profile;

  @ManyToMany
  @JoinTable(
    name = "student_courses_course",
    joinColumns = @JoinColumn(name = "student_id"),
    inverseJoinColumns = @JoinColumn(name = "course_id")
  )
  @JsonIgnoreProperties({"students", "assignments"})
  @Builder.Default
  private Set<CourseEntity> courses = new HashSet<>();

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

  // Soft delete: null = active, timestamp = deleted.
  private LocalDateTime deletedAt;

  @PrePersist
  void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}
