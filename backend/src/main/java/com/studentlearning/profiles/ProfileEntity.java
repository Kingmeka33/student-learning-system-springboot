package com.studentlearning.profiles;

import com.studentlearning.students.StudentEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "profile")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProfileEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(nullable = false)
  private String bio;

  private String avatarUrl;

  @OneToOne
  @JoinColumn(name = "student_id", unique = true)
  @JsonIgnore
  private StudentEntity student;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @PrePersist void onCreate() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }
  @PreUpdate void onUpdate() { updatedAt = LocalDateTime.now(); }
}
