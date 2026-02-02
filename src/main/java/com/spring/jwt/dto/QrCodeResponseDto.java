package com.spring.jwt.dto;

import com.spring.jwt.entity.QrCode.QrStatus;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QrCodeResponseDto {

    private UUID id;

    private String qrValue;

    private QrStatus status;

    private Instant createdAt;

    private Integer businessId;

    private String businessName;
}
