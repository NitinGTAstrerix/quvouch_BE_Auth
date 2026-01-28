package com.spring.jwt.repository;

import com.spring.jwt.entity.Review;
import com.spring.jwt.entity.Review.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    List<Review> findByBusiness_BusinessId(Integer businessId);

    long countByBusiness_BusinessIdAndStatus(Integer businessId, ReviewStatus status);
}