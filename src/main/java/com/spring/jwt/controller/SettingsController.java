package com.spring.jwt.controller;

import com.spring.jwt.dto.ChangePasswordDTO;
import com.spring.jwt.dto.SettingsProfileDTO;
import com.spring.jwt.dto.UpdateProfileDTO;
import com.spring.jwt.service.SettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/settings")
@RequiredArgsConstructor
@Tag(name = "Settings Controller", description = "APIs for managing user settings")
public class SettingsController {

    private final SettingsService settingsService;

    @Operation(summary = "Get user profile", description = "Fetch the profile details of the currently logged-in user")
    @GetMapping("/profile")
    public ResponseEntity<SettingsProfileDTO> getProfile() {
        return ResponseEntity.ok(settingsService.getProfile());
    }

    @Operation(summary = "Update profile", description = "Allows the logged-in user to update their profile details such as name, mobile number, and address")
    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(
            @Valid @RequestBody UpdateProfileDTO request) {

        settingsService.updateProfile(request);
        return ResponseEntity.ok("Profile updated successfully");
    }

    @Operation(summary = "Change password", description = "Allows the logged-in user to change their account password")
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody ChangePasswordDTO request) {

        settingsService.changePassword(request);
        return ResponseEntity.ok("Password changed successfully");
    }
}