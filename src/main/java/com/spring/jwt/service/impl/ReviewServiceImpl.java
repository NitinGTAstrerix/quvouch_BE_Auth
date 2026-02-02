package com.spring.jwt.service.impl;

import com.spring.jwt.dto.ReviewResponse;
import com.spring.jwt.dto.ReviewStatsDTO;
import com.spring.jwt.entity.Business;
import com.spring.jwt.entity.Review;
import com.spring.jwt.dto.ReviewRequestDto;
import com.spring.jwt.mapper.ReviewMapper;
import com.spring.jwt.repository.BusinessRepository;
import com.spring.jwt.repository.ReviewRepository;
import com.spring.jwt.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final BusinessRepository businessRepository;
    private final ReviewMapper reviewMapper;

    @Override
    @Transactional
    public Review submitReview(ReviewRequestDto requestDto) {

        Business business = businessRepository.findById(requestDto.getBusinessId())
                .orElseThrow(() -> new RuntimeException("Business not found with ID: " + requestDto.getBusinessId()));

        Review review = new Review();
        review.setBusiness(business);
        review.setQrCodeId(requestDto.getQrCodeId());
        review.setRating(requestDto.getRating());
        review.setCustomerName(requestDto.getCustomerName());
        review.setCustomerEmail(requestDto.getCustomerEmail());
        review.setCustomerPhone(requestDto.getCustomerPhone());
        review.setFeedbackText(requestDto.getFeedbackText());
        review.setFeedbackCategory(requestDto.getFeedbackCategory());

        String clientGoogleUrl = "https://search.google.com/local/writereview?placeid=EXAMPLE";

        if (requestDto.getRating() >= 4) {
            review.setStatus(Review.ReviewStatus.PUBLIC);
            review.setRedirectUrl(clientGoogleUrl);
        } else {
            review.setStatus(Review.ReviewStatus.INTERNAL);
            review.setRedirectUrl(null);
        }

        return reviewRepository.save(review);
    }

    @Override
    public List<ReviewResponse> getAllReviews() {
        return reviewRepository.findAll()
                .stream()
                .map(reviewMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewResponse> getReviewsByBusiness(Integer businessId) {
        return reviewRepository.findByBusiness_BusinessId(businessId)
                .stream()
                .map(reviewMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewStatsDTO getReviewStatistics(Integer businessId) {
        return reviewRepository.getReviewStatistics(businessId);
    }
}