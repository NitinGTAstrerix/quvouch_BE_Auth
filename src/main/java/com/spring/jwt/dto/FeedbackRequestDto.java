package com.spring.jwt.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class FeedbackRequestDto {

    @NotBlank(message = "Name required")
    private String name;

    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "Message required")
    private String message;

    @Min(1)
    @Max(5)
    private Integer rating;

    private Integer businessId;
}
