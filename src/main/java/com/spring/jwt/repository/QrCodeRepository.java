package com.spring.jwt.repository;

import com.spring.jwt.entity.QrCode;
import com.spring.jwt.entity.User;
import com.spring.jwt.entity.QrCode.QrStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QrCodeRepository extends JpaRepository<QrCode, String> {

    long countByBusiness_UserAndStatus(User user, QrStatus status);

    List<QrCode> findByBusiness_UserAndStatus(User user, QrStatus status);
}
