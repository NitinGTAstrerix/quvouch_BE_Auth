package com.spring.jwt.service.impl;

import com.spring.jwt.dto.*;
import com.spring.jwt.entity.Business;
import com.spring.jwt.entity.QrCode;
import com.spring.jwt.entity.Review;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.BaseException;
import com.spring.jwt.exception.BusinessNotFound;
import com.spring.jwt.mapper.BusinessMapper;
import com.spring.jwt.repository.BusinessRepository;
import com.spring.jwt.repository.QrCodeRepository;
import com.spring.jwt.repository.ReviewRepository;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.dto.BusinessDashboardDto;
import com.spring.jwt.dto.RatingDistributionDto;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import com.spring.jwt.service.BusinessService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@AllArgsConstructor
@Slf4j
@Service
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository businessRepository;
    private final ReviewRepository reviewRepository;
    private final QrCodeRepository qrCodeRepository;
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

    @Override
    public BusinessDashboardDto getDashboardData(Integer businessId) {

        // 1️⃣ Get Business
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found"));

        // 2️⃣ Review Stats
        ReviewStatsDTO stats =
                reviewRepository.getReviewStatistics(businessId);

        Long totalReviews = (stats != null && stats.getTotalReviews() != null)
                ? stats.getTotalReviews()
                : 0L;

        Double averageRating = (stats != null && stats.getAverageRating() != null)
                ? stats.getAverageRating()
                : 0.0;

        // 3️⃣ Total QR Scans (using your existing method)
        Long totalScans = qrCodeRepository.getTotalScans(business);
        if (totalScans == null) {
            totalScans = 0L;
        }

        // 4️⃣ Active QR Codes (using your existing method)
        Long activeQrCodes =
                qrCodeRepository.countByBusinessAndActiveTrue(business);

        if (activeQrCodes == null) {
            activeQrCodes = 0L;
        }

        // 5️⃣ Rating Distribution
        List<Object[]> distribution =
                reviewRepository.getRatingDistribution(businessId);

        List<RatingDistributionDto> ratingList = distribution.stream()
                .map(obj -> new RatingDistributionDto(
                        (Integer) obj[0],
                        (Long) obj[1]))
                .toList();

        return new BusinessDashboardDto(
                totalReviews,
                averageRating,
                totalScans,
                activeQrCodes,
                ratingList
        );
    }

    @Override
    public byte[] exportReviewsAsCsv(Integer businessId) {

        List<Review> reviews =
                reviewRepository.findByBusiness_BusinessId(businessId);

        StringBuilder sb = new StringBuilder();

        sb.append("Customer Name,Email,Phone,Rating,Feedback,Category,Status,Created At\n");

        for (Review r : reviews) {

            sb.append(safe(r.getCustomerName())).append(",");
            sb.append(safe(r.getCustomerEmail())).append(",");
            sb.append(safe(r.getCustomerPhone())).append(",");
            sb.append(r.getRating()).append(",");
            sb.append("\"").append(safe(r.getFeedbackText())).append("\"").append(",");
            sb.append(safe(r.getFeedbackCategory())).append(",");
            sb.append(r.getStatus()).append(",");
            sb.append(r.getCreatedAt()).append("\n");
        }

        return sb.toString().getBytes();
    }

    private String safe(String value) {
        return value == null ? "" : value.replace(",", " ");
    }



    @Override
    public String generateShareLink(Integer businessId) {
        return "https://quvouch.com/reviews/public/" + businessId;
    }

    @Override
    public List<Object[]> getMonthlyAnalytics(Integer businessId) {
        return (List<Object[]>) reviewRepository.getMonthlyAnalytics(businessId);
    }

    @Override
    public UrlResource downloadQrCode(Integer businessId) {

        QrCode qrCode = qrCodeRepository.findByBusiness_BusinessId(businessId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("QR Code not found"));

        try {

            Path tempFile = Files.createTempFile("business-qr-", ".png");
            Files.write(tempFile, qrCode.getQrImage());

            return new UrlResource(tempFile.toUri());

        } catch (Exception e) {
            throw new RuntimeException("Failed to load QR code", e);
        }
    }
}