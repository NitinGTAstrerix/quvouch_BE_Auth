package com.spring.jwt.service;

import com.spring.jwt.dto.FeedbackRequestDto;
import com.spring.jwt.dto.FeedbackResponseDto;

import java.util.List;

public interface FeedbackService {

    FeedbackResponseDto saveFeedback(FeedbackRequestDto request);

    List<FeedbackResponseDto> getAllFeedback();

    FeedbackResponseDto getFeedback(Long id);

    void deleteFeedback(Long id);
}
