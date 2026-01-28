package com.spring.jwt.dto;

import com.spring.jwt.entity.Business;
import com.spring.jwt.entity.User;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BusinessResponseDto {

    private Integer businessId;

    private String businessName;
    private String businessType;
    private String address;
    private Long phoneNumber;
    private Instant createdAt;
    private Instant updatedAt;

    private Integer userId;
}
