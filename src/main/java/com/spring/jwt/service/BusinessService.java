package com.spring.jwt.service;

import com.spring.jwt.dto.BusinessRequestDto;
import com.spring.jwt.dto.BusinessResponseDto;
import com.spring.jwt.dto.UserProfileDTO;
import com.spring.jwt.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BusinessService {

    BusinessResponseDto createBusiness(BusinessRequestDto businessRequestDto);

    BusinessResponseDto getBusinessById(Integer businessId);

    List<BusinessResponseDto> getAllBusiness();

    Page<BusinessResponseDto> getAllBusinessByPageNumber(int pageNo, int pageSize);

    User getCurrentUserProfile();

    BusinessResponseDto updateBusiness(Integer businessId, BusinessRequestDto businessRequestDto);
}
