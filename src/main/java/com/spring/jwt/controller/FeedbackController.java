package com.spring.jwt.controller;

import com.spring.jwt.dto.FeedbackRequestDto;
import com.spring.jwt.dto.FeedbackResponseDto;
import com.spring.jwt.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<FeedbackResponseDto> saveFeedback(
            @Valid @RequestBody FeedbackRequestDto request) {

        return ResponseEntity.ok(feedbackService.saveFeedback(request));
    }
}
