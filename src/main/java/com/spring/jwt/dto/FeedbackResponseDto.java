package com.spring.jwt.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class FeedbackResponseDto {

    private Long id;
    private String name;
    private String email;
    private String message;
    private Integer rating;
    private LocalDateTime createdAt;
}
