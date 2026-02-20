package com.spring.jwt.repository;

import com.spring.jwt.dto.AdminQrCodeDTO;
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

    @Query("""
SELECT new com.spring.jwt.dto.AdminQrCodeDTO(
    q.id,
    COALESCE(b.businessName,'Unassigned'),
    q.location,
    q.scanCount,
    COUNT(r),
    q.qrLink,
    q.qrCodePath,
    q.status,
    q.createdAt
)
FROM QrCode q
LEFT JOIN q.business b
LEFT JOIN Review r ON CAST(r.qrCodeId AS string) = q.id
GROUP BY q.id, b.businessName, q.location, q.scanCount, q.qrLink, q.qrCodePath, q.status, q.createdAt
ORDER BY q.createdAt DESC
""")
    List<AdminQrCodeDTO> getAllQrCodesForAdmin();
}