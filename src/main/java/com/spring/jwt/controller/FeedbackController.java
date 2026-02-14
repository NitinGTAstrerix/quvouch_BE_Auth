package com.spring.jwt.controller;

import com.spring.jwt.dto.FeedbackRequestDto;
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
    public ResponseEntity<?> createFeedback(
            @Valid @RequestBody FeedbackRequestDto request) {

        return ResponseEntity.ok(
                feedbackService.saveFeedback(request));
    }

    @GetMapping
    public ResponseEntity<?> getAllFeedback() {
        return ResponseEntity.ok(
                feedbackService.getAllFeedback());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFeedback(@PathVariable Long id) {
        return ResponseEntity.ok(
                feedbackService.getFeedbackById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.ok("Deleted successfully");
    }
}
