package com.spring.jwt.repository;

import com.spring.jwt.entity.Business;
import com.spring.jwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business, Integer> {

    Optional<Business> findByUser_Id(Integer Id);

    List<Business> findByBusinessNameContainingIgnoreCase(String name);

    List<Business> findByUser(User user);

    Optional<Business> findByBusinessIdAndUser(Integer id, User user);

    List<Business> findByUserAndBusinessNameContainingIgnoreCase(
            User user,
            String keyword
    );

    long countByUser(User user);

    long countByUserAndStatus(User user, Business.BusinessStatus status);

    List<Business> findByUserAndStatus(User user, Business.BusinessStatus status);

    @Query("""
        SELECT FUNCTION('MONTH', r.createdAt),
               COUNT(r)
        FROM Review r
        WHERE r.business.businessId = :businessId
        GROUP BY FUNCTION('MONTH', r.createdAt)
       """)
    List<Object[]> getMonthlyAnalytics(@Param("businessId") Integer businessId);

    List<Business> findByUser_IdIn(List<Integer> userIds);

}
