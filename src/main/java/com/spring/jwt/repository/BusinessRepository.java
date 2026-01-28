package com.spring.jwt.repository;

import com.spring.jwt.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessRepository extends JpaRepository<Business, Integer> {
}
