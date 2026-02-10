package com.spring.jwt.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
public class FeedbackRequestDto {

    @NotBlank(message = "Name required")
    private String name;

    @Email
    @NotBlank(message = "Email required")
    private String email;

    @NotBlank(message = "Message required")
    private String message;

    @Min(1)
    @Max(5)
    private Integer rating;
}
