package com.spring.jwt.controller;

import com.spring.jwt.dto.FeedbackRequestDto;
import com.spring.jwt.dto.FeedbackResponseDto;
import com.spring.jwt.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public FeedbackResponseDto saveFeedback(@RequestBody FeedbackRequestDto dto) {
        return feedbackService.saveFeedback(dto);
    }

    @GetMapping
    public List<FeedbackResponseDto> getAllFeedback() {
        return feedbackService.getAllFeedback();
    }

    @GetMapping("/{id}")
    public FeedbackResponseDto getFeedback(@PathVariable Long id) {
        return feedbackService.getFeedbackById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return "Feedback deleted successfully";
    }
}
