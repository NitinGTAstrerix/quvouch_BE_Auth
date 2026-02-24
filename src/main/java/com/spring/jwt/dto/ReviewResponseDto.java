package com.spring.jwt.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ReviewResponseDto {

    private UUID reviewId;
    private String customerName;
    private Integer rating;
    private String comment;
    private String location;
    private LocalDateTime createdAt;
}