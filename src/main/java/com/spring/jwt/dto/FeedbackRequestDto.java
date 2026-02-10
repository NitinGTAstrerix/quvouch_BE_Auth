package com.spring.jwt.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email required")
    private String email;

    @NotBlank(message = "Message required")
    private String message;

    @Min(value = 1, message = "Rating min 1")
    @Max(value = 5, message = "Rating max 5")
    private Integer rating;
}
