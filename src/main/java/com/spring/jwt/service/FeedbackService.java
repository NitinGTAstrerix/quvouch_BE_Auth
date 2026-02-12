package com.spring.jwt.service;

import com.spring.jwt.dto.FeedbackRequestDto;
import com.spring.jwt.dto.FeedbackResponseDto;

import java.util.List;

public interface FeedbackService {

    // SAVE FEEDBACK
    FeedbackResponseDto saveFeedback(FeedbackRequestDto request);

    // GET ALL FEEDBACK
    List<FeedbackResponseDto> getAllFeedback();

    //  DELETE FEEDBACK
    void deleteFeedback(Long id);
}
