package com.spring.jwt.dto;

import com.spring.jwt.entity.Business;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ClientDetailsDTO {

    private Integer businessId;
    private String businessName;
    private String businessType;
    private String address;
    private String phoneNumber;
    private String businessEmail;
    private Business.BusinessStatus status;
    private Instant createdAt;

    private Long totalQrCodes;
    private Long totalReviews;
    private Double averageRating;
}