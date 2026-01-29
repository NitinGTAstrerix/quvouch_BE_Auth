package com.spring.jwt.repository;

import com.spring.jwt.dto.ReviewStatsDTO;
import com.spring.jwt.entity.Review;
import com.spring.jwt.entity.Review.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    List<Review> findByBusiness_BusinessId(Integer businessId);

    long countByBusiness_BusinessIdAndStatus(Integer businessId, ReviewStatus status);

    @Query("SELECT new com.spring.jwt.dto.ReviewStatsDTO(" +
            "  COUNT(r), " +
            "  SUM(CASE WHEN r.status = 'PUBLIC' THEN 1 ELSE 0 END), " +
            "  SUM(CASE WHEN r.status = 'INTERNAL' THEN 1 ELSE 0 END), " +
            "  AVG(r.rating) " +
            ") " +
            "FROM Review r " +
            "WHERE r.business.businessId = :businessId")
    ReviewStatsDTO getReviewStatistics(@Param("businessId") Integer businessId);
}