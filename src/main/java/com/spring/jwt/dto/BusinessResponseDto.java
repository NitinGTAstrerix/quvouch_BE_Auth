package com.spring.jwt.dto;

import com.spring.jwt.entity.Business;
import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BusinessResponseDto {

    private Integer businessId;
    private String businessName;
    private String businessType;
    private String address;
    private Long phoneNumber;
    private Business.BusinessStatus status;
    private Instant createdAt;
    private Instant updatedAt;

    private Integer userId;
}
