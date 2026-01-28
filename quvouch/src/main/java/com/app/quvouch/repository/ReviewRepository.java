package com.app.quvouch.repository;

import com.app.quvouch.entity.Review;
import com.app.quvouch.entity.Review.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    List<Review> findByBusiness_BusinessId(UUID businessId);

    long countByBusiness_BusinessIdAndStatus(UUID businessId, ReviewStatus status);
}