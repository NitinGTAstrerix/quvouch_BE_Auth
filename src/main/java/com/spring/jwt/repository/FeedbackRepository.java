package com.spring.jwt.repository;

import com.spring.jwt.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    boolean existsByEmailAndBusiness_BusinessId(String email, Integer businessId);

    @Query("""  
        SELECT f FROM Feedback f
        JOIN f.business b
        JOIN b.user u
        WHERE u.saleRepresentative.id = :saleRepId
    """)
    List<Feedback> findReviewsBySaleRep(@Param("saleRepId") Integer saleRepId);

    List<Feedback> findByBusiness_BusinessIdIn(List<Integer> businessIds);


}
