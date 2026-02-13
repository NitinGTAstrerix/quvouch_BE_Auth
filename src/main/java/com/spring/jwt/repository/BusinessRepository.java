package com.spring.jwt.repository;

import com.spring.jwt.entity.Business;
import com.spring.jwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

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

}
