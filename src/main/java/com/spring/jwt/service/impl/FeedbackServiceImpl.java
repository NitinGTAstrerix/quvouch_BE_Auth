package com.spring.jwt.service.impl;

import com.spring.jwt.dto.FeedbackRequestDto;
import com.spring.jwt.dto.FeedbackResponseDto;
import com.spring.jwt.entity.Business;
import com.spring.jwt.entity.Feedback;
import com.spring.jwt.exception.BusinessNotFound;
import com.spring.jwt.exception.FeedbackAlreadyAvaliable;
import com.spring.jwt.repository.BusinessRepository;
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
    private final BusinessRepository businessRepository;

    @Override
    public FeedbackResponseDto saveFeedback(FeedbackRequestDto request) {

        if (feedbackRepository.existsByEmailAndBusiness_BusinessId(
                request.getEmail(), request.getBusinessId())) {

            throw new FeedbackAlreadyAvaliable("Feedback already submitted");
        }

        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1-5");
        }
        Business business = businessRepository.findById(request.getBusinessId()).orElseThrow(() -> new BusinessNotFound("Business is not found"));

        Feedback feedback = Feedback.builder()
                .name(request.getName())
                .email(request.getEmail())
                .message(request.getMessage())
                .rating(request.getRating())
                .business(business)
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
