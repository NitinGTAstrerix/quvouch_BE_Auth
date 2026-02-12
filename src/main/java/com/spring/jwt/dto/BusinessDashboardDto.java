package com.spring.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessDashboardDto {

    private Long totalReviews;
    private Double averageRating;
    private Long totalScans;
    private Long activeQrCodes;

    private List<RatingDistributionDto> ratingDistribution;
}
