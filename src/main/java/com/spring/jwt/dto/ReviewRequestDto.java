package com.spring.jwt.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.UUID;

@Data
public class ReviewRequestDto {

    @NotNull(message = "Business ID is required")
    private Integer businessId;

    @NotNull(message = "QR Code ID is required")
    private UUID qrCodeId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @NotNull(message = "Customer Name is required")
    private String customerName;

    @NotBlank(message = "Customer email is required")
    @Email(message = "Invalid email format")
    private String customerEmail;

    @Pattern(regexp = "^\\d{10}$", message = "Customer phone must be exactly 10 digits")
    private String customerPhone;

    private String feedbackText;
    private String feedbackCategory;
}