package com.spring.jwt.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FeedbackResponseDto {

    private Long id;
    private String name;
    private String email;
    private String message;
    private Integer rating;

    // Timestamp returned in response
    private LocalDateTime createdAt;

    private Integer businessId;
}
