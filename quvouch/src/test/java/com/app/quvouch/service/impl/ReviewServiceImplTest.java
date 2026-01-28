package com.app.quvouch.service.impl;

import com.app.quvouch.entity.Business;
import com.app.quvouch.entity.Review;
import com.app.quvouch.entity.Review.ReviewStatus;
import com.app.quvouch.dtos.ReviewRequestDto;
import com.app.quvouch.exception.BusinessNotFoundException;
import com.app.quvouch.repository.BusinessRepository;
import com.app.quvouch.repository.ReviewRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private BusinessRepository businessRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private ReviewRequestDto requestDto;
    private Business mockBusiness;

    @BeforeEach
    void setUp() {
        UUID businessId = UUID.randomUUID();

        mockBusiness = new Business();
        mockBusiness.setBusinessId(businessId);
        mockBusiness.setBusinessName("Test Cafe");

        requestDto = new ReviewRequestDto();
        requestDto.setBusinessId(businessId);
        requestDto.setQrCodeId(UUID.randomUUID());
    }

    @Test
    @DisplayName("Logic: 5 Stars -> PUBLIC Status & Redirect URL Set")
    void testSubmitReview_PublicLogic() {
        requestDto.setRating(5);
        when(businessRepository.findById(any())).thenReturn(Optional.of(mockBusiness));
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> i.getArguments()[0]);

        Review result = reviewService.submitReview(requestDto);

        assertEquals(ReviewStatus.PUBLIC, result.getStatus());
        assertNotNull(result.getRedirectUrl());
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    @DisplayName("Logic: 2 Stars -> INTERNAL Status & Redirect URL Null")
    void testSubmitReview_InternalLogic() {
        requestDto.setRating(2);
        requestDto.setFeedbackText("Too slow");

        when(businessRepository.findById(any())).thenReturn(Optional.of(mockBusiness));
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> i.getArguments()[0]);

        Review result = reviewService.submitReview(requestDto);

        assertEquals(ReviewStatus.INTERNAL, result.getStatus());
        assertNull(result.getRedirectUrl());
    }

    @Test
    @DisplayName("Error: Should throw exception if Business not found")
    void testSubmitReview_BusinessNotFound() {
        when(businessRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(BusinessNotFoundException.class, () -> {
            reviewService.submitReview(requestDto);
        });
    }
}