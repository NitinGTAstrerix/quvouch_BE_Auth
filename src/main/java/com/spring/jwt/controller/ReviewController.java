package com.spring.jwt.controller;

import com.spring.jwt.dto.ReviewRequestDto;
import com.spring.jwt.dto.ReviewResponse;
import com.spring.jwt.dto.ReviewStatsDTO;
import com.spring.jwt.entity.Review;
import com.spring.jwt.mapper.ReviewMapper;
import com.spring.jwt.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/qr")
@RequiredArgsConstructor
@Tag(name = "Review Controller", description = "Endpoints for Review Submission, Retrieval, and Analytics")
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    @Operation(summary = "Submit a Review", description = "Public endpoint for customers to rate a business via QR code.")
    @PostMapping("/{qrCodeId}/rate")
    public ResponseEntity<ReviewResponse> submitReview(
            @PathVariable UUID qrCodeId,
            @RequestBody @Valid ReviewRequestDto requestDto
    ) {
        requestDto.setQrCodeId(qrCodeId);
        Review savedReview = reviewService.submitReview(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reviewMapper.toResponse(savedReview));
    }

    @Operation(summary = "Get All Reviews (Admin)", description = "Retrieve a list of ALL reviews in the system.")
    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewResponse>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @Operation(summary = "Get Reviews by Business", description = "Retrieve a list of reviews for a specific business ID.")
    @GetMapping("/reviews/business/{businessId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByBusiness(@PathVariable Integer businessId) {
        return ResponseEntity.ok(reviewService.getReviewsByBusiness(businessId));
    }

    @Operation(summary = "Get Review Statistics", description = "Get total reviews, public/internal split, and average rating for a business.")
    @GetMapping("/reviews/business/{businessId}/stats")
    public ResponseEntity<ReviewStatsDTO> getReviewStats(@PathVariable Integer businessId) {
        ReviewStatsDTO stats = reviewService.getReviewStatistics(businessId);
        return ResponseEntity.ok(stats);
    }

<<<<<<< HEAD
    @GetMapping("/qr/{qrCodeId}")
=======
    @GetMapping("/{qrCodeId}")
>>>>>>> fec191664a493331f12a1e1fed807664e99cd9c4
    public ResponseEntity<List<ReviewResponse>> getByQrCode(
            @PathVariable UUID qrCodeId) {

        return ResponseEntity.ok(
                reviewService.getReviewsByQrCode(qrCodeId)
        );
    }
}