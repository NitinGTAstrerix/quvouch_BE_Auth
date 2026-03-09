package com.spring.jwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettingsProfileDTO {

    @NotNull(message = "User ID is required")
    private Integer id;

    @Schema(description = "User first name", example = "Rahul")
    @Pattern(regexp = "^[A-Za-z]{2,50}$", message = "First name must contain only letters and be 2-50 characters long")
    private String firstName;

    @Schema(description = "User last name", example = "Patil")
    @Pattern(regexp = "^[A-Za-z]{2,50}$", message = "Last name must contain only letters and be 2-50 characters long")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(description = "User mobile number", example = "9988776655")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone number must be a valid 10-digit Indian mobile number")
    private String mobileNumber;

    @NotBlank(message = "Address is required")
    @Schema(description = "User address", example = "Hinjewadi Phase 1, Pune")
    private String address;

    @NotBlank(message = "Role is required")
    private String role;
}