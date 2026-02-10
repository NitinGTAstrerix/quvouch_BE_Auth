package com.spring.jwt.service.impl;

import com.spring.jwt.dto.FeedbackRequestDto;
import com.spring.jwt.dto.FeedbackResponseDto;
import com.spring.jwt.entity.Feedback;
import com.spring.jwt.repository.FeedbackRepository;
import com.spring.jwt.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Override
    public FeedbackResponseDto saveFeedback(FeedbackRequestDto request) {

        // ⭐ Prevent duplicate feedback
        if (feedbackRepository.existsByEmailAndBusinessId(
                request.getEmail(), request.getBusinessId())) {

            throw new RuntimeException("Feedback already submitted");
        }

        // ⭐ Rating validation
        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1-5");
        }

        Feedback feedback = Feedback.builder()
                .name(request.getName())
                .email(request.getEmail())
                .message(request.getMessage())
                .rating(request.getRating())
                .businessId(request.getBusinessId())
                .createdAt(LocalDateTime.now())
                .build();

        feedbackRepository.save(feedback);

        return FeedbackResponseDto.builder()
                .id(feedback.getId())
                .message("Feedback saved successfully")
                .createdAt(feedback.getCreatedAt())
                .build();
    }

    @Override
    public List<FeedbackResponseDto> getAllFeedback() {

        return feedbackRepository.findAll().stream()
                .map(f -> FeedbackResponseDto.builder()
                        .id(f.getId())
                        .message(f.getMessage())
                        .createdAt(f.getCreatedAt())
                        .build())
                .toList();
    }

    @Override
    public FeedbackResponseDto getFeedbackById(Long id) {

        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));

        return FeedbackResponseDto.builder()
                .id(feedback.getId())
                .message(feedback.getMessage())
                .createdAt(feedback.getCreatedAt())
                .build();
    }

    @Override
    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }
}
