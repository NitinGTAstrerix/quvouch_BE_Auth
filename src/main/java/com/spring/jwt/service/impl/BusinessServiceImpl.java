package com.spring.jwt.service.impl;

import com.spring.jwt.dto.BusinessRequestDto;
import com.spring.jwt.dto.BusinessResponseDto;
import com.spring.jwt.dto.UserProfileDTO;
import com.spring.jwt.entity.Business;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.BaseException;
import com.spring.jwt.exception.BusinessNotFound;
import com.spring.jwt.mapper.BusinessMapper;
import com.spring.jwt.repository.BusinessRepository;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.service.BusinessService;
import com.spring.jwt.utils.BaseResponseDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository businessRepository;
    private final BusinessMapper mapper;
    private final UserRepository userRepository;

    @Override
    public BusinessResponseDto createBusiness(BusinessRequestDto businessRequestDto) {

        if (ObjectUtils.isEmpty(businessRequestDto.getBusinessName()))
        {
            throw new BaseException(String.valueOf(HttpStatus.NOT_FOUND),"Please Enter Business Name");
        }
        Business business = mapper.toBusinessRequest(businessRequestDto);
        business.setUser(getCurrentUserProfile());
        Business saved = businessRepository.save(business);
        return mapper.toBusiness(saved);
    }

    @Override
    public BusinessResponseDto getBusinessById(Integer businessId) {
        Business business = businessRepository.findById(businessId).orElseThrow(() -> new UsernameNotFoundException("Business is Not Found"));
        return mapper.toBusiness(business);
    }

    @Override
    public BusinessResponseDto getBusinessByOwn() {
        Integer id = getCurrentUserProfile().getId();
        Business business = businessRepository.findByUser_Id(id).orElseThrow(() -> new BusinessNotFound("Business not found"));
        return mapper.toBusiness(business);
    }

    @Override
    public List<BusinessResponseDto> getAllBusiness() {
        List<Business> allBusiness = businessRepository.findAll();

        return allBusiness.stream().map(mapper::toBusiness).toList();
    }

    @Override
    public Page<BusinessResponseDto> getAllBusinessByPageNumber(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Business> all = businessRepository.findAll(pageable);
        return all.map(mapper::toBusiness);
    }

    @Override
    public User getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
        {
            throw new BaseException(String.valueOf(HttpStatus.UNAUTHORIZED.value()),"User is not Authenticated");
        }
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        if (user == null)
        {
            throw new UsernameNotFoundException("User is not found");
        }

        return user;
    }

    @Override
    public BusinessResponseDto updateBusiness(Integer businessId, BusinessRequestDto businessRequestDto) {
        Business business = businessRepository.findById(businessId).orElseThrow(() -> new BusinessNotFound("Business is not found"));

        if (businessRequestDto.getBusinessName() != null){
            business.setBusinessName(businessRequestDto.getBusinessName());
        }
        if(businessRequestDto.getBusinessType() != null)
        {
            business.setBusinessType(businessRequestDto.getBusinessType());
        }
        if (businessRequestDto.getPhoneNumber()!=null)
        {
            business.setPhoneNumber(businessRequestDto.getPhoneNumber());
        }
        if (businessRequestDto.getAddress() != null)
        {
            business.setAddress(businessRequestDto.getAddress());
        }
        Business updateBusiness = businessRepository.save(business);

        return mapper.toBusiness(updateBusiness);
    }


}
