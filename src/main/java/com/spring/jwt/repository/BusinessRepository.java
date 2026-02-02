package com.spring.jwt.repository;

import com.spring.jwt.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business, Integer> {
    Optional<Business> findByUser_Id(Integer userId);
}
