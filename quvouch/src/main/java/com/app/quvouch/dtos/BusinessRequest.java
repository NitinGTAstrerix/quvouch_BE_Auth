package com.app.quvouch.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessRequest {
    private UUID businessId;
    private String businessName;
    private String businessType;
    private String businessAddress;
    private Long phoneNumber;
}
