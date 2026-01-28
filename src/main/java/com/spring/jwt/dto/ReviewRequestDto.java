package com.spring.jwt.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String feedbackText;
    private String feedbackCategory;
}