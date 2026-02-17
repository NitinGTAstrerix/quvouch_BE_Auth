package com.spring.jwt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignQrCodeRequest {

    @NotBlank(message = "QR Code ID is required")
    private String qrCodeId;

    @NotNull(message = "Client ID is required")
    private Integer clientId;

    private String locationLabel;
}