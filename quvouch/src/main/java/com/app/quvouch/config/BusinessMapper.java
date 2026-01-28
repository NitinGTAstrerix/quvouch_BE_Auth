package com.app.quvouch.config;

import com.app.quvouch.entity.Business;
import com.app.quvouch.dtos.BusinessResponse;
import com.app.quvouch.dtos.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class BusinessMapper {

    public BusinessResponse ToBusinessResponse(Business b)
    {
        return BusinessResponse.builder()
                .businessId(b.getBusinessId())
                .businessType(b.getBusinessType())
                .businessAddress(b.getBusinessAddress())
                .businessName(b.getBusinessName())
                .phoneNumber(b.getPhoneNumber())
                .owner(b.getUser() == null ? null : UserResponse.builder()
                        .name(b.getUser().getName())
                        .email(b.getUser().getEmail())
                        .id(b.getUser().getId()).build()).build();
    }
}
