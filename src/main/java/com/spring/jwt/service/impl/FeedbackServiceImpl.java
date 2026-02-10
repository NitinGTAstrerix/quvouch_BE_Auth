package com.spring.jwt.service.impl;

import com.spring.jwt.dto.FeedbackRequestDto;
import com.spring.jwt.dto.FeedbackResponseDto;
import com.spring.jwt.entity.Feedback;
import com.spring.jwt.repository.FeedbackRepository;
import com.spring.jwt.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

        Feedback saved = feedbackRepository.save(feedback);

        return FeedbackResponseDto.builder()
                .id(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .message(saved.getMessage())
                .rating(saved.getRating())
                .createdAt(saved.getCreatedAt())
                .build();
    }
}
