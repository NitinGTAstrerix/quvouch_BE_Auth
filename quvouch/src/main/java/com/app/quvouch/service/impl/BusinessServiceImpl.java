package com.app.quvouch.service.impl;

import com.app.quvouch.entity.Business;
import com.app.quvouch.entity.User;
import com.app.quvouch.config.BusinessMapper;
import com.app.quvouch.dtos.BusinessRequest;
import com.app.quvouch.dtos.BusinessResponse;
import com.app.quvouch.exception.BusinessNotFoundException;
import com.app.quvouch.repository.BusinessRepository;
import com.app.quvouch.service.AuthService;
import com.app.quvouch.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository businessRepository;
    private final ModelMapper modelMapper;
    private final AuthService authService;
    private final BusinessMapper businessMapper;

    @Override
    public BusinessRequest createBusiness(BusinessRequest businessRequest) {

        Business business = modelMapper.map(businessRequest, Business.class);
        User user = authService.CurrentUser();
        business.setUser(user);
        Business savedBusiness = businessRepository.save(business);

        return modelMapper.map(savedBusiness,BusinessRequest.class);
    }

    @Override
    public BusinessResponse getBusinessById(UUID businessId) {

        Business business = businessRepository.findById(businessId).orElseThrow(() -> new BusinessNotFoundException("At the Given id business is not exits"));

        return businessMapper.ToBusinessResponse(business);
    }

    @Override
    public Page<BusinessResponse> getAllBusiness(Pageable pageable) {

        return businessRepository
                .findAll(pageable)
                .map(businessMapper::ToBusinessResponse);
    }

    @Override
    public String deleteBusiness(UUID businessId) {
        Business business = businessRepository.findById(businessId).orElseThrow(() -> new BusinessNotFoundException("Business is not found "));
        businessRepository.delete(business);
        return "Business is deleted";
    }

    @Override
    public BusinessRequest updateBusiness(UUID businessId, BusinessRequest businessRequest) {
        Business exitingBusiness = businessRepository.findById(businessId).orElseThrow(() -> new BusinessNotFoundException("Business not Found "));
        if (businessRequest.getBusinessName() !=null) exitingBusiness.setBusinessName(businessRequest.getBusinessName());
        if (businessRequest.getBusinessType() !=null) exitingBusiness.setBusinessType(businessRequest.getBusinessType());
        if (businessRequest.getBusinessAddress() !=null) exitingBusiness.setBusinessAddress(businessRequest.getBusinessAddress());
        if (businessRequest.getPhoneNumber() != null) exitingBusiness.setPhoneNumber(businessRequest.getPhoneNumber());

        Business updateBusiness = businessRepository.save(exitingBusiness);
        return modelMapper.map(updateBusiness,BusinessRequest.class);
    }

    @Override
    public Page<BusinessResponse> getBusinessByName(String keyword, Pageable pageable) {
        return businessRepository.findByBusinessNameContainingIgnoreCase(keyword, pageable).map(businessMapper::ToBusinessResponse);
    }
}
