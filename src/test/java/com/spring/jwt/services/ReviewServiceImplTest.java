package com.spring.jwt.services;

import com.spring.jwt.dto.ReviewRequestDto;
import com.spring.jwt.dto.ReviewStatsDTO;
import com.spring.jwt.entity.Business;
import com.spring.jwt.entity.Review;
import com.spring.jwt.entity.Review.ReviewStatus;
import com.spring.jwt.mapper.ReviewMapper;
import com.spring.jwt.repository.BusinessRepository;
import com.spring.jwt.repository.ReviewRepository;
import com.spring.jwt.service.impl.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private BusinessRepository businessRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private ReviewRequestDto requestDto;
    private Business mockBusiness;
    private Integer businessId = 10001;

    @BeforeEach
    void setUp() {
        mockBusiness = new Business();
        mockBusiness.setBusinessId(businessId);
        mockBusiness.setBusinessName("Test Cafe");

        requestDto = new ReviewRequestDto();
        requestDto.setBusinessId(businessId);
        requestDto.setQrCodeId(UUID.randomUUID());
    }

    @Test
    @DisplayName("Logic: 5 Stars -> PUBLIC Status")
    void testSubmitReview_Public() {
        requestDto.setRating(5);

        when(businessRepository.findById(businessId)).thenReturn(Optional.of(mockBusiness));
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> i.getArguments()[0]);

        Review result = reviewService.submitReview(requestDto);

        assertEquals(ReviewStatus.PUBLIC, result.getStatus());
        assertNotNull(result.getRedirectUrl());
    }

    @Test
    @DisplayName("Logic: 2 Stars -> INTERNAL Status")
    void testSubmitReview_Internal() {
        requestDto.setRating(2);

        when(businessRepository.findById(businessId)).thenReturn(Optional.of(mockBusiness));
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> i.getArguments()[0]);

        Review result = reviewService.submitReview(requestDto);

        assertEquals(ReviewStatus.INTERNAL, result.getStatus());
        assertNull(result.getRedirectUrl());
    }

    @Test
    @DisplayName("Error: Business Not Found")
    void testSubmitReview_BusinessNotFound() {
        when(businessRepository.findById(businessId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            reviewService.submitReview(requestDto);
        });

        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    @DisplayName("Stats: Should retrieve statistics correctly")
    void testGetReviewStatistics() {

        ReviewStatsDTO mockStats = new ReviewStatsDTO(50, 40L, 10L, 4.2);
        when(reviewRepository.getReviewStatistics(businessId)).thenReturn(mockStats);

        ReviewStatsDTO result = reviewService.getReviewStatistics(businessId);

        assertNotNull(result);
        assertEquals(50, result.getTotalReviews());
        assertEquals(4.2, result.getAverageRating());

        verify(reviewRepository, times(1)).getReviewStatistics(businessId);
    }
}