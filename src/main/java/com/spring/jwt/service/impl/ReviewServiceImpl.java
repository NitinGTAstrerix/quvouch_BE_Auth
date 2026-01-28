package com.spring.jwt.service.impl;

import com.app.quvouch.entity.Business;
import com.app.quvouch.entity.Review;
import com.app.quvouch.dtos.ReviewRequestDto;
import com.app.quvouch.exception.BusinessNotFoundException;
import com.app.quvouch.repository.BusinessRepository;
import com.app.quvouch.repository.ReviewRepository;
import com.app.quvouch.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final BusinessRepository businessRepository;

    @Override
    @Transactional
    public Review submitReview(ReviewRequestDto requestDto) {

        Business business = businessRepository.findById(requestDto.getBusinessId())
                .orElseThrow(() -> new BusinessNotFoundException("Business not found with ID: " + requestDto.getBusinessId()));

        Review review = new Review();
        review.setBusiness(business);
        review.setQrCodeId(requestDto.getQrCodeId());
        review.setRating(requestDto.getRating());
        review.setCustomerName(requestDto.getCustomerName());
        review.setCustomerEmail(requestDto.getCustomerEmail());
        review.setCustomerPhone(requestDto.getCustomerPhone());
        review.setFeedbackText(requestDto.getFeedbackText());
        review.setFeedbackCategory(requestDto.getFeedbackCategory());

        // TODO: In the future, fetch this URL from 'business.getGoogleReviewUrl()'
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
}