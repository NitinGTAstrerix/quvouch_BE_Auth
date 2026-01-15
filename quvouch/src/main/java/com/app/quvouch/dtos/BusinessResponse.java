package com.app.quvouch.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessResponse {
    private UUID businessId;
    private String businessName;
    private String businessType;
    private String businessAddress;
    private Long phoneNumber;
    private UserResponse owner;
}
