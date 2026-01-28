package com.spring.jwt.mapper;

import com.spring.jwt.entity.Review;
import com.spring.jwt.dto.ReviewResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    private final ModelMapper modelMapper;

    public ReviewMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ReviewResponse toResponse(Review review) {
        // 1. Automatic mapping for matching fields (id, rating, etc.)
        ReviewResponse response = modelMapper.map(review, ReviewResponse.class);

        // 2. Manual mapping for nested Business fields (Safety check)
        if (review.getBusiness() != null) {
            response.setBusinessId(review.getBusiness().getBusinessId());
            response.setBusinessName(review.getBusiness().getBusinessName());
        }
        return response;
    }
}