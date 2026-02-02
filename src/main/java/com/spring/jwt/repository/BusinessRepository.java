package com.spring.jwt.repository;

import com.spring.jwt.entity.Business;
import com.spring.jwt.entity.BusinessStatus;
import com.spring.jwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business, Integer> {

    List<Business> findByUser(User user);

    List<Business> findByUserAndBusinessNameContainingIgnoreCase(
            User user,
            String keyword
    );

    Optional<Business> findByBusinessIdAndUser(Integer id, User user);

    long countByUser(User user);

    long countByUserAndStatus(User user, BusinessStatus status);

    List<Business> findByUserAndStatus(User user, BusinessStatus status);

}
