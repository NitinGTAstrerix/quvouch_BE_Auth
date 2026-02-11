package com.spring.jwt.repository;

import com.spring.jwt.entity.Business;
import com.spring.jwt.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    boolean existsByEmailAndBusiness(String email, Business business);
}
