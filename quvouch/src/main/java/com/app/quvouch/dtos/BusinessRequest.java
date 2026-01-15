package com.app.quvouch.dtos;

import com.app.quvouch.Models.Business;
import com.app.quvouch.Models.User;
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
