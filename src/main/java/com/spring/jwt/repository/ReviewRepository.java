package com.spring.jwt.repository;

import com.spring.jwt.dto.MonthlyAnalyticsDTO;
import com.spring.jwt.dto.ReviewStatsDTO;
import com.spring.jwt.entity.QrCode;
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

    List<Review> findByQrCodeId(UUID qrCodeId);
    @Query("""
        SELECT r.rating, COUNT(r)
        FROM Review r
        WHERE r.business.businessId = :businessId
        GROUP BY r.rating
       """)
    List<Object[]> getRatingDistribution(@Param("businessId") Integer businessId);

    @Query("""
SELECT new com.spring.jwt.dto.MonthlyAnalyticsDTO(
    MONTH(r.createdAt),
    YEAR(r.createdAt),
    COUNT(r),
    AVG(r.rating),
    SUM(CASE WHEN r.status = 'PUBLIC' THEN 1 ELSE 0 END),
    SUM(CASE WHEN r.status = 'INTERNAL' THEN 1 ELSE 0 END),
    SUM(CASE WHEN r.rating >= 4 THEN 1 ELSE 0 END),
    SUM(CASE WHEN r.rating <= 2 THEN 1 ELSE 0 END)
)
FROM Review r
WHERE r.business.businessId = :businessId
GROUP BY YEAR(r.createdAt), MONTH(r.createdAt)
ORDER BY YEAR(r.createdAt), MONTH(r.createdAt)
""")
    List<MonthlyAnalyticsDTO> getMonthlyAnalytics(@Param("businessId") Integer businessId);
}