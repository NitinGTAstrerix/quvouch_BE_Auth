package com.spring.jwt.repository;

import com.spring.jwt.entity.QrCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QrCodeRepository extends JpaRepository<QrCode,String> {
}
