package com.spring.jwt.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewUpdateRequestDto {

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    private Integer rating;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone number must be a valid 10-digit Indian mobile number")
    private String customerPhone;

    @NotBlank(message = "Feedback text is required")
    private String feedbackText;

    private String feedbackCategory;
}