package com.app.quvouch.service;

import com.app.quvouch.dtos.BusinessRequest;
import com.app.quvouch.dtos.BusinessResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BusinessService {

    BusinessRequest createBusiness(BusinessRequest businessRequest);

    BusinessResponse getBusinessById(UUID businessId);

    Page<BusinessResponse> getAllBusiness(Pageable pageable);

    String deleteBusiness(UUID businessId);

    BusinessRequest updateBusiness(UUID businessId, BusinessRequest businessRequest);

    Page<BusinessResponse> getBusinessByName(String keyword, Pageable pageable);
}
