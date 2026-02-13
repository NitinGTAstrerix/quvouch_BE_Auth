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
    private String phoneNumber;
    private Business.BusinessStatus status;
<<<<<<< HEAD
=======
    private String businessEmail;
>>>>>>> fec191664a493331f12a1e1fed807664e99cd9c4
    private Instant createdAt;
    private Instant updatedAt;

    private Integer userId;
}
