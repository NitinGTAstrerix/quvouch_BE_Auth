package com.spring.jwt.dto;

import com.spring.jwt.entity.Business;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    private Integer businessId;

    private LocalDateTime createdAt;
}
