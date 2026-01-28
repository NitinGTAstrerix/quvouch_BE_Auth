package com.app.quvouch.service;

import com.app.quvouch.entity.Review;
import com.app.quvouch.dtos.ReviewRequestDto;

public interface ReviewService {
    Review submitReview(ReviewRequestDto requestDto);
}