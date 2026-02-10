package com.spring.jwt.controller;

import com.spring.jwt.dto.*;
import com.spring.jwt.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<FeedbackResponseDto>> getAllFeedback() {
        return ResponseEntity.ok(feedbackService.getAllFeedback());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeedbackResponseDto> getFeedback(@PathVariable Long id) {
        return ResponseEntity.ok(feedbackService.getFeedback(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.ok("Deleted successfully");
    }
}
