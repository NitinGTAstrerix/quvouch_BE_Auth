package com.spring.jwt.service.impl;

import com.spring.jwt.dto.*;
import com.spring.jwt.entity.Feedback;
import com.spring.jwt.repository.FeedbackRepository;
import com.spring.jwt.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Override
    public FeedbackResponseDto saveFeedback(FeedbackRequestDto request) {

        Feedback feedback = Feedback.builder()
                .name(request.getName())
                .email(request.getEmail())
                .message(request.getMessage())
                .rating(request.getRating())
                .createdAt(LocalDateTime.now())
                .build();

        feedbackRepository.save(feedback);

        return mapToDto(feedback);
    }

    @Override
    public List<FeedbackResponseDto> getAllFeedback() {
        return feedbackRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public FeedbackResponseDto getFeedback(Long id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));

        return mapToDto(feedback);
    }

    @Override
    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }

    private FeedbackResponseDto mapToDto(Feedback feedback) {
        return FeedbackResponseDto.builder()
                .id(feedback.getId())
                .name(feedback.getName())
                .email(feedback.getEmail())
                .message(feedback.getMessage())
                .rating(feedback.getRating())
                .createdAt(feedback.getCreatedAt())
                .build();
    }
}
