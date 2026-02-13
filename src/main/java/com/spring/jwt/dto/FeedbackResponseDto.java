package com.spring.jwt.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FeedbackResponseDto {

    private Long id;
    private String message;
    private LocalDateTime createdAt;
}
