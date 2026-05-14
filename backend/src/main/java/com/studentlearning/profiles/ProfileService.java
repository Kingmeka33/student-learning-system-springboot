package com.studentlearning.profiles;

import com.studentlearning.common.dto.IdRequest;
import com.studentlearning.common.dto.SearchRequest;
import com.studentlearning.common.exception.ApiException;
import com.studentlearning.profiles.ProfileDtos.*;
import com.studentlearning.students.StudentEntity;
import com.studentlearning.students.StudentService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
  private final ProfileRepository profileRepository;
  private final StudentService studentService;
  public ProfileService(ProfileRepository profileRepository, StudentService studentService) {
    this.profileRepository = profileRepository;
    this.studentService = studentService;
  }
  public ProfileEntity create(CreateProfileRequest request) {
    try {
      if (profileRepository.existsByStudentId(request.studentId())) throw new ApiException(HttpStatus.CONFLICT, "StudentEntity already has a profile");
      StudentEntity student = studentService.findOne(new IdRequest(request.studentId()));
      ProfileEntity profile = ProfileEntity.builder().bio(request.bio()).avatarUrl(request.avatarUrl()).student(student).build();
      return profileRepository.save(profile);
    } catch (ApiException ex) { throw ex; }
    catch (Exception ex) { throw new ApiException(HttpStatus.BAD_REQUEST, ex.getMessage()); }
  }
  public List<ProfileEntity> findAll(SearchRequest request) {
    String search = request == null ? null : request.search();
    if (search != null && !search.isBlank()) return profileRepository.findByBioContainingIgnoreCase(search);
    return profileRepository.findAll();
  }
  public ProfileEntity findOne(IdRequest request) { return profileRepository.findById(request.id()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "ProfileEntity not found")); }
  public ProfileEntity update(IdRequest idRequest, UpdateProfileRequest request) {
    ProfileEntity profile = findOne(idRequest);
    if (request.bio() != null && !request.bio().isBlank()) profile.setBio(request.bio());
    if (request.avatarUrl() != null) profile.setAvatarUrl(request.avatarUrl());
    if (request.studentId() != null && !request.studentId().isBlank()) profile.setStudent(studentService.findOne(new IdRequest(request.studentId())));
    return profileRepository.save(profile);
  }
  public void delete(IdRequest request) { profileRepository.delete(findOne(request)); }
}
