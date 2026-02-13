package com.spring.jwt.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.spring.jwt.dto.ReviewStatsDTO;
import com.spring.jwt.entity.Business;
import com.spring.jwt.entity.Review;
import com.spring.jwt.entity.Review.ReviewStatus;
import com.spring.jwt.utils.EncryptionUtil;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Import(EncryptionUtil.class)
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Test
    @DisplayName("Find Reviews by Business ID")
    void testFindReviewsByBusinessId() {

        Business business = new Business();
        business.setBusinessName("Repo Test Cafe");

        business = businessRepository.save(business);

        Integer businessId = business.getBusinessId();

        Review review = new Review();

        review.setId(UUID.randomUUID());
        review.setBusiness(business);
        review.setQrCodeId(UUID.randomUUID());
        review.setRating(5);
        review.setStatus(ReviewStatus.PUBLIC);
        review.setCreatedAt(LocalDateTime.now());
        review.setFeedbackText("Excellent service!");

        reviewRepository.save(review);

        List<Review> results =
                reviewRepository.findByBusiness_BusinessId(businessId);

        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getRating()).isEqualTo(5);
        assertThat(results.get(0).getBusiness().getBusinessId()).isEqualTo(businessId);
    }

    @Test
    @DisplayName("Calculate Statistics: Public, Internal, and Average")
    void testGetReviewStatistics() {

        // Create Business
        Business business = new Business();
        business.setBusinessName("Stats Burger Joint");

        business = businessRepository.save(business);

        Integer businessId = business.getBusinessId();

        createReview(business, 5, ReviewStatus.PUBLIC);
        createReview(business, 4, ReviewStatus.PUBLIC);
        createReview(business, 2, ReviewStatus.INTERNAL);

        ReviewStatsDTO stats =
                reviewRepository.getReviewStatistics(businessId);

        assertThat(stats).isNotNull();
        assertThat(stats.getTotalReviews()).isEqualTo(3);
        assertThat(stats.getPublicReviews()).isEqualTo(2);
        assertThat(stats.getInternalReviews()).isEqualTo(1);
        assertThat(stats.getAverageRating()).isCloseTo(
                        3.67,
                        org.assertj.core.data.Offset.offset(0.1)
                );
    }

    private void createReview(
            Business business,
            int rating,
            ReviewStatus status
    ) {

        Review review = new Review();

        review.setId(UUID.randomUUID());
        review.setBusiness(business);
        review.setQrCodeId(UUID.randomUUID());
        review.setRating(rating);
        review.setStatus(status);
        review.setCreatedAt(LocalDateTime.now());
        review.setFeedbackText("Test Review");

        reviewRepository.save(review);
    }
}
