package com.spring.jwt.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewStatsDTO {
    private long totalReviews;
    private long publicReviews;
    private long internalReviews;
    private double averageRating;

    public ReviewStatsDTO(long totalReviews, Long publicReviews, Long internalReviews, Double averageRating) {
        this.totalReviews = totalReviews;
        this.publicReviews = (publicReviews != null) ? publicReviews : 0L;
        this.internalReviews = (internalReviews != null) ? internalReviews : 0L;
        this.averageRating = (averageRating != null) ? averageRating : 0.0;
    }
}