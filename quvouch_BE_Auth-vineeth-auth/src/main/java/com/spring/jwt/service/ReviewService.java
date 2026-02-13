package com.spring.jwt.service;

import com.spring.jwt.dto.ReviewRequestDto;
import com.spring.jwt.dto.ReviewResponse;
import com.spring.jwt.dto.ReviewStatsDTO;
import com.spring.jwt.entity.Review;

import java.util.List;
import java.util.UUID;

public interface ReviewService {

    Review submitReview(ReviewRequestDto requestDto);

    List<ReviewResponse> getAllReviews();

    List<ReviewResponse> getReviewsByBusiness(Integer businessId);

    ReviewStatsDTO getReviewStatistics(Integer businessId);

    List<ReviewResponse> getReviewsByQrCode(UUID qrCodeId);

}