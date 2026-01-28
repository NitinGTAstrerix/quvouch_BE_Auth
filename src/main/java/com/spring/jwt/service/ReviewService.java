package com.spring.jwt.service;

import com.spring.jwt.entity.Review;
import com.spring.jwt.dto.ReviewRequestDto;

public interface ReviewService {

    Review submitReview(ReviewRequestDto requestDto);
}