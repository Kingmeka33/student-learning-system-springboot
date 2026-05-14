package com.studentlearning.auth;

import com.studentlearning.common.exception.ApiException;
import com.studentlearning.students.StudentEntity;
import com.studentlearning.students.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final AppUserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final StudentRepository studentRepository;

  public AuthService(
      AppUserRepository userRepository,
      PasswordEncoder passwordEncoder,
      JwtService jwtService,
      StudentRepository studentRepository
  ) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.studentRepository = studentRepository;
  }

  public AuthResponse register(RegisterRequest request) {
    try {
      if (userRepository.existsByEmail(request.email())) {
        throw new ApiException(HttpStatus.CONFLICT, "Email is already registered");
      }

      AppUserEntity user = AppUserEntity.builder()
          .name(request.name())
          .email(request.email())
          .passwordHash(passwordEncoder.encode(request.password()))
          .role(request.role())
          .build();

      AppUserEntity saved = userRepository.save(user);

      if (saved.getRole() == UserRole.STUDENT && !studentRepository.existsByEmail(saved.getEmail())) {
        StudentEntity student = StudentEntity.builder()
            .name(saved.getName())
            .email(saved.getEmail())
            .build();

        studentRepository.save(student);
      }

      return response(saved);
    } catch (ApiException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new ApiException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
  }

  public AuthResponse login(LoginRequest request) {
    AppUserEntity user = userRepository.findByEmail(request.email())
        .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

    if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
    }

    return response(user);
  }

  private AuthResponse response(AppUserEntity user) {
    return new AuthResponse(
        jwtService.generateToken(user),
        new AuthResponse.UserInfo(user.getId(), user.getName(), user.getEmail(), user.getRole())
    );
  }
}
