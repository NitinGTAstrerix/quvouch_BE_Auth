package com.spring.jwt.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AdminRecentReviewDto {

    private Integer rating;
    private String businessName;
    private String comment;
    private String customerName;
    private LocalDateTime time;
}