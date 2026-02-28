package com.spring.jwt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QrGenerateRequest {

    @NotNull(message = "Business ID is required")
    private Integer businessId;

    @NotBlank(message = "Location is required")
    private String location;

}
