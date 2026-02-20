package com.spring.jwt.dto;

import com.spring.jwt.entity.QrCode.QrStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AdminQrCodeDTO {

    private String qrId;

    private String businessName;

    private String location;

    private Integer scanCount;

    private Long totalReviews;

    private String qrLink;

    private String qrCodePath;

    private QrStatus status;

    private LocalDateTime createdAt;

}