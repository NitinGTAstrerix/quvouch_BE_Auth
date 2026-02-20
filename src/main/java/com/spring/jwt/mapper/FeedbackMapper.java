package com.spring.jwt.mapper;

import com.spring.jwt.dto.FeedbackResponseDto;
import com.spring.jwt.entity.Feedback;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FeedbackMapper {

    public FeedbackResponseDto toDto(Feedback f) {
        return FeedbackResponseDto.builder()
                .name(f.getName())
                .email(f.getEmail())
                .businessId(f.getBusiness().getBusinessId())
                .id(f.getId())
                .createdAt(f.getCreatedAt())
                .message(f.getMessage())
                .rating(f.getRating())
                .build();
    }
}
