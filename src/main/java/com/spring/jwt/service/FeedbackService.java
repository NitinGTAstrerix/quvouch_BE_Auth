package com.spring.jwt.service;

import com.spring.jwt.dto.FeedbackRequestDto;
import com.spring.jwt.dto.FeedbackResponseDto;

public interface FeedbackService {

    FeedbackResponseDto saveFeedback(FeedbackRequestDto request);
}
