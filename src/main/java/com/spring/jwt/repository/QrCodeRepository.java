package com.spring.jwt.repository;

import com.spring.jwt.entity.Business;
import com.spring.jwt.entity.QrCode;
import com.spring.jwt.entity.User;
import com.spring.jwt.entity.QrCode.QrStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QrCodeRepository extends JpaRepository<QrCode, String> {

    long countByBusiness_UserAndStatus(User user, QrStatus status);

    List<QrCode> findByBusiness_UserAndStatus(User user, QrStatus status);

    @Query("""
        SELECT COALESCE(SUM(q.scanCount),0)
        FROM QrCode q
        WHERE q.business = :business
       """)
    Long getTotalScans(@Param("business") Business business);

    Long countByBusinessAndActiveTrue(Business business);

    Optional<QrCode> findByBusiness_BusinessId(Integer business);

    List<QrCode> findByStatus(QrCode.QrStatus status);

    List<QrCode> findByAssignedBy(User user);

    List<QrCode> findByBusiness(Business business);

}
