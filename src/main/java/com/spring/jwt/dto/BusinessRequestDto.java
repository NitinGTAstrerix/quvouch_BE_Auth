package com.spring.jwt.dto;

import com.spring.jwt.entity.User;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessRequestDto {

    @NotNull(message = "Business Name is required")
    @Size(max = 100, min = 3, message = "Business must be greater then 3 and less than 100")
    private String businessName;

    @NotNull(message = "Business Type is required")
    private String businessType;

    @NotNull(message = "Address is required")
    private String address;

    @NotNull(message = "Phone number is required")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Phone number must be a valid 10-digit Indian mobile number"
    )
    private Long phoneNumber;

    @NotBlank(message = "Customer email is required")
    @Email(message = "Invalid email format")
    private String businessEmail;
}