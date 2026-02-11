package com.spring.jwt.service.impl;

import com.spring.jwt.dto.FeedbackRequestDto;
import com.spring.jwt.dto.FeedbackResponseDto;
import com.spring.jwt.entity.Business;
import com.spring.jwt.entity.Feedback;
import com.spring.jwt.repository.BusinessRepository;
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
    private final BusinessRepository businessRepository;

    @Override
    public FeedbackResponseDto saveFeedback(FeedbackRequestDto request) {

        // Convert Integer → Integer (Business API safe)
        Integer businessId = request.getBusinessId();

        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found"));

        if (feedbackRepository.existsByEmailAndBusiness(request.getEmail(), business)) {
            throw new RuntimeException("Feedback already submitted");
        }

        Feedback feedback = Feedback.builder()
                .name(request.getName())
                .email(request.getEmail())
                .message(request.getMessage())
                .rating(request.getRating())
                .createdAt(LocalDateTime.now())
                .business(business)
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
    public FeedbackResponseDto getFeedbackById(Long id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));
        return mapToDto(feedback);
    }

    @Override
    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }

    private FeedbackResponseDto mapToDto(Feedback f) {
        return FeedbackResponseDto.builder()
                .id(f.getId())
                .name(f.getName())
                .email(f.getEmail())
                .message(f.getMessage())
                .rating(f.getRating())
                .createdAt(f.getCreatedAt())
                .businessId(f.getBusiness() != null ? f.getBusiness().getBusinessId() : null)
                .build();
    }
}
