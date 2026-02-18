package com.spring.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyAnalyticsDTO {

    private Integer month;
    private Integer year;
    private Long totalReviews;
    private Double averageRating;
    private Long publicReviews;
    private Long internalReviews;
    private Long positiveReviews;
    private Long negativeReviews;
}
