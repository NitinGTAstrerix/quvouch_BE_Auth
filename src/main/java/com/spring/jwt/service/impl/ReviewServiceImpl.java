package com.spring.jwt.service.impl;

import com.spring.jwt.dto.FeedbackResponseDto;
import com.spring.jwt.dto.ReviewResponse;
import com.spring.jwt.dto.ReviewStatsDTO;
import com.spring.jwt.entity.Business;
import com.spring.jwt.entity.Feedback;
import com.spring.jwt.entity.Review;
import com.spring.jwt.dto.ReviewRequestDto;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.ResourceNotFoundException;
import com.spring.jwt.mapper.FeedbackMapper;
import com.spring.jwt.mapper.ReviewMapper;
import com.spring.jwt.repository.BusinessRepository;
import com.spring.jwt.repository.FeedbackRepository;
import com.spring.jwt.repository.ReviewRepository;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.service.ReviewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final BusinessRepository businessRepository;
    private final ReviewMapper reviewMapper;
    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;

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

    @Override
    public List<ReviewResponse> getReviewsByQrCode(UUID qrCodeId) {

        List<Review> reviews = reviewRepository.findByQrCodeId(qrCodeId);

        return reviews.stream()
                .map(reviewMapper::toResponse)
                .toList();
    }


    @Override
    public List<FeedbackResponseDto> getReviewsBySaleRep(Integer saleRepId) {

        // 1️⃣ get clients
        List<User> clients = userRepository.findBySaleRepresentative_Id(saleRepId);
        log.info("All clients {}",clients);
        if (clients == null || clients.isEmpty()) {
            log.info("No clients found for saleRep {}", saleRepId);
            return List.of();
        }

        List<Integer> clientIds = clients.stream()
                .map(User::getId)
                .toList();

        // 2️⃣ get businesses
        List<Business> businesses = businessRepository.findByUser_IdIn(clientIds);

        if (businesses == null || businesses.isEmpty()) {
            log.info("No businesses found for clients {}", clientIds);
            return List.of();
        }

        List<Integer> businessIds = businesses.stream()
                .map(Business::getBusinessId)
                .toList();

        // 3️⃣ get feedbacks
        List<Feedback> feedbacks =
                feedbackRepository.findByBusiness_BusinessIdIn(businessIds);

        if (feedbacks == null || feedbacks.isEmpty()) {
            log.info("No feedback found for businesses {}", businessIds);
            return List.of();
        }

        // 4️⃣ map DTO
        return feedbacks.stream()
                .map(feedbackMapper::toDto)
                .toList();
    }
}