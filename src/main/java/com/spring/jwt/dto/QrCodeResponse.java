package com.spring.jwt.dto;

import lombok.Builder;

@Builder
public record QrCodeResponse(String id, String qrLink ,String location,Integer scanCount ,boolean active, Integer businessId) {
    public QrCodeResponse {
    }
}
