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

        ReviewResponse response = modelMapper.map(review, ReviewResponse.class);

        if (review.getBusiness() != null) {
            response.setBusinessId(review.getBusiness().getBusinessId());
            response.setBusinessName(review.getBusiness().getBusinessName());
        }
        return response;
    }
}