package com.spring.jwt.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class FeedbackRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    // EMAIL VALIDATION ADDED HERE
    @Email(message = "Email format should be user@gmail.com")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Message cannot be empty")
    private String message;

    //  RATING VALIDATION ADDED
    @Min(value = 1, message = "Rating min 1")
    @Max(value = 5, message = "Rating max 5")
    private Integer rating;

    // Business ID comes from request
    private Integer businessId;
}
