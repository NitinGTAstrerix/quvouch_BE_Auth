package com.spring.jwt.controller;

import com.app.quvouch.entity.Review;
import com.app.quvouch.dtos.ReviewRequestDto;
import com.app.quvouch.dtos.ReviewResponse;
import com.app.quvouch.service.ReviewService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/qr")
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ModelMapper modelMapper;

    @PostMapping("/{qrCodeId}/rate")
    public ResponseEntity<ReviewResponse> submitReview(
            @PathVariable UUID qrCodeId,
            @RequestBody @Valid ReviewRequestDto requestDto
    ) {
        requestDto.setQrCodeId(qrCodeId);

        Review savedReview = reviewService.submitReview(requestDto);

        ReviewResponse response = modelMapper.map(savedReview, ReviewResponse.class);

        response.setBusinessId(savedReview.getBusiness().getBusinessId());
        response.setBusinessName(savedReview.getBusiness().getBusinessName());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}