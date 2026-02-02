package com.spring.jwt.repository;

import com.spring.jwt.entity.QrCode;
import com.spring.jwt.entity.QrCode.QrStatus;
import com.spring.jwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface QrCodeRepository extends JpaRepository<QrCode, UUID> {

    // Get active QRs for logged-in sales rep
    List<QrCode> findByBusiness_UserAndStatus(
            User user,
            QrStatus status
    );

    long countByBusiness_UserAndStatus(
            User user,
            QrStatus status
    );
}
