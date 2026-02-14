package com.spring.jwt.dto;

import com.spring.jwt.entity.Review.ReviewStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private UUID id;
    private Integer businessId;
    private String businessName;
    private UUID qrCodeId;
    private Integer rating;
    private String customerName;
    private String feedbackText;
    private ReviewStatus status;
    private String redirectUrl;
    private LocalDateTime createdAt;
}