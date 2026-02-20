package com.spring.jwt.mapper;

import com.spring.jwt.dto.BusinessRequestDto;
import com.spring.jwt.dto.BusinessResponseDto;
import com.spring.jwt.entity.Business;
import org.springframework.stereotype.Component;

@Component
public class BusinessMapper {
    public BusinessResponseDto toBusiness(Business business) {
        return BusinessResponseDto.builder()
                .businessId(business.getBusinessId())
                .businessName(business.getBusinessName())
                .businessType(business.getBusinessType())
                .address(business.getAddress())
                .phoneNumber(business.getPhoneNumber())
                .businessEmail(business.getBusinessEmail())
                .status(business.getStatus())
                .createdAt(business.getCreatedAt())
                .updatedAt(business.getUpdatedAt())
                .userId(business.getUser().getId())
                .build();
    }

    public Business toBusinessRequest(BusinessRequestDto businessRequestDto)
    {
        return Business.builder()
                .businessName(businessRequestDto.getBusinessName())
                .businessType(businessRequestDto.getBusinessType())
                .address(businessRequestDto.getAddress())
                .phoneNumber(businessRequestDto.getPhoneNumber())
                .businessEmail(businessRequestDto.getBusinessEmail())
                .status(Business.BusinessStatus.ACTIVE)
                .build();
    }

}
