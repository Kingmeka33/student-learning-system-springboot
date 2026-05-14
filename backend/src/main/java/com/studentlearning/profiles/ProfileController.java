package com.studentlearning.profiles;
import com.studentlearning.common.security.SecurityRoleNames;

import com.studentlearning.common.dto.DeleteResponse;
import com.studentlearning.common.dto.IdRequest;
import com.studentlearning.common.dto.SearchRequest;
import com.studentlearning.profiles.ProfileDtos.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Profiles", description = "StudentEntity profile endpoints for the one-to-one relationship.")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/profiles")
public class ProfileController {
  private final ProfileService profileService;
  public ProfileController(ProfileService profileService) { this.profileService = profileService; }

  @Operation(summary = "Get profiles", description = "Supports optional search by bio.")
  @GetMapping
  public ResponseEntity<List<ProfileEntity>> findAll(@RequestParam(required = false) String search) {
    return ResponseEntity.ok(profileService.findAll(new SearchRequest(search)));
  }

  @Operation(summary = "Get one profile by UUID")
  @GetMapping("/{id}")
  public ResponseEntity<ProfileEntity> findOne(@PathVariable String id) {
    return ResponseEntity.ok(profileService.findOne(new IdRequest(id)));
  }

  @Operation(summary = "Create a profile", description = "ADMIN only. A student can only have one profile.")
  @PostMapping
  @PreAuthorize("hasRole('" + SecurityRoleNames.ADMIN_ROLE + "')")
  public ResponseEntity<ProfileEntity> create(@Valid @RequestBody CreateProfileRequest request) {
    ProfileEntity profile = profileService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(profile);
  }

  @Operation(summary = "Update a profile", description = "ADMIN only.")
  @PatchMapping("/{id}")
  @PreAuthorize("hasRole('" + SecurityRoleNames.ADMIN_ROLE + "')")
  public ResponseEntity<ProfileEntity> update(@PathVariable String id, @Valid @RequestBody UpdateProfileRequest request) {
    return ResponseEntity.ok(profileService.update(new IdRequest(id), request));
  }

  @Operation(summary = "Delete a profile", description = "ADMIN only.")
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('" + SecurityRoleNames.ADMIN_ROLE + "')")
  public ResponseEntity<DeleteResponse> delete(@PathVariable String id) {
    profileService.delete(new IdRequest(id));
    return ResponseEntity.ok(new DeleteResponse("ProfileEntity deleted successfully"));
  }
}
