package com.spring.jwt.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.spring.jwt.utils.EncryptionUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.spring.jwt.entity.Business;
import com.spring.jwt.entity.Review;
import com.spring.jwt.entity.Review.ReviewStatus;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
@Import(EncryptionUtil.class)
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Test
    void testFindReviewsByBusinessId() {
        Business business = new Business();
        business.setBusinessName("Repo Test Cafe");
        business = businessRepository.save(business);

        Review review = new Review();
        review.setBusiness(business);
        review.setQrCodeId(UUID.randomUUID());
        review.setRating(5);
        review.setStatus(ReviewStatus.PUBLIC);
        review.setCreatedAt(LocalDateTime.now());
        review.setFeedbackText("Excellent service!");

        reviewRepository.save(review);

        List<Review> results = reviewRepository.findByBusiness_BusinessId(business.getBusinessId());

        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getRating()).isEqualTo(5);
        assertThat(results.get(0).getBusiness().getBusinessId()).isEqualTo(business.getBusinessId());
    }
}