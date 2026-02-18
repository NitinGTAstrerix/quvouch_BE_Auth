package com.spring.jwt.dto;

import com.spring.jwt.entity.Business;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ClientListDTO {

    private Integer businessId;
    private String businessName;
    private String businessType;
    private String phoneNumber;
    private String businessEmail;
    private Business.BusinessStatus status;
    private Instant createdAt;

    private Long totalQrCodes;
    private Long totalReviews;
}