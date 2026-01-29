package com.spring.jwt.service;

import com.spring.jwt.entity.Review;
import com.spring.jwt.dto.ReviewRequestDto;
import com.spring.jwt.dto.ReviewResponse; // ✅ Added Import

import java.util.List; // ✅ Added Import

public interface ReviewService {

    Review submitReview(ReviewRequestDto requestDto);

    List<ReviewResponse> getAllReviews();

    List<ReviewResponse> getReviewsByBusiness(Integer businessId);
}
