package com.spring.jwt.service;

import com.spring.jwt.dto.*;
import com.spring.jwt.entity.User;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BusinessService {

    BusinessResponseDto createBusiness(BusinessRequestDto businessRequestDto);

    BusinessResponseDto getBusinessById(Integer businessId);

    BusinessResponseDto getBusinessByOwn();

    List<BusinessResponseDto> getAllBusiness();

    Page<BusinessResponseDto> getAllBusinessByPageNumber(int pageNo, int pageSize);

    User getCurrentUserProfile();

    BusinessResponseDto updateBusiness(Integer businessId, BusinessRequestDto businessRequestDto);

    BusinessDashboardDto getDashboardData(Integer businessId);

    byte[] exportReviewsAsCsv(Integer businessId);

    byte[] exportReviewsAsPdf(Integer businessId);

    String generateShareLink(Integer businessId);

    List<MonthlyAnalyticsDTO> getMonthlyAnalytics(Integer businessId);

    UrlResource downloadQrCode(Integer businessId);

    List<ReviewResponseDto> getMyBusinessReviews(Integer rating, String keyword);

    String deleteBusiness(Integer businessId);
}