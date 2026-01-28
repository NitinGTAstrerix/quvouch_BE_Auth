package com.app.quvouch.repository;

import com.app.quvouch.entity.Business;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BusinessRepository extends JpaRepository<Business, UUID> {
    Page<Business> findByBusinessNameContainingIgnoreCase(String keyword, Pageable pageable);
}
