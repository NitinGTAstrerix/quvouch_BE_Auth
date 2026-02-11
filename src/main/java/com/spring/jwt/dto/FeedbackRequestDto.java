package com.spring.jwt.dto;

import lombok.Data;

@Data
public class FeedbackRequestDto {

    private String name;
    private String email;
    private String message;
    private Integer rating;

    // IMPORTANT → keeping Integer to match Business API
    private Integer businessId;
}
