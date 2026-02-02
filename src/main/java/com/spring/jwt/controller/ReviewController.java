package com.spring.jwt.controller;

import com.spring.jwt.entity.Review;
import com.spring.jwt.mapper.ReviewMapper;
import com.spring.jwt.dto.ReviewRequestDto;
import com.spring.jwt.dto.ReviewResponse;
import com.spring.jwt.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/qr")
@AllArgsConstructor
@Tag(name = "Review Controller", description = "Endpoints for Public Review Submission")
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    @Operation(summary = "Submit a Review", description = "Public endpoint for customers to rate a business.")
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
}