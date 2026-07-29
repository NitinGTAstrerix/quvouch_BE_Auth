package com.spring.jwt.repository;

import com.spring.jwt.dto.AdminQrCodeDTO;
import com.spring.jwt.entity.Business;
import com.spring.jwt.entity.QrCode;
import com.spring.jwt.entity.User;
import com.spring.jwt.entity.QrCode.QrStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QrCodeRepository extends JpaRepository<QrCode, String> {

    @Query("""
        SELECT COALESCE(SUM(q.scanCount),0)
        FROM QrCode q
        WHERE q.business = :business
       """)
    Long getTotalScans(@Param("business") Business business);

    Long countByBusinessAndActiveTrue(Business business);

    Long countByBusiness(Business business);

    Optional<QrCode> findByBusiness_BusinessId(Integer business);

    @Query("""
SELECT new com.spring.jwt.dto.AdminQrCodeDTO(
    q.id,
    COALESCE(b.businessName,'Unassigned'),
    q.location,
    q.scanCount,
    COUNT(r),
    q.qrLink,
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

    boolean existsByBusinessAndLocationIgnoreCase(Business business, String location);

    List<QrCode> findByAssignedBy(User assignedBy);

    List<QrCode> findByBusinessInAndStatus(List<Business> businesses, QrCode.QrStatus status);

    List<QrCode> findByBusinessIn(List<Business> businesses);

    @Query("""
SELECT COUNT(q)
FROM QrCode q
WHERE q.business.businessId = :businessId
AND q.active = true
""")
    Long countActiveQr(@Param("businessId") Integer businessId);

    @Transactional
    void deleteByBusiness_BusinessId(Integer businessId);

    @Query("""
    SELECT COUNT(q)
    FROM QrCode q
    WHERE q.active = true
""")
    Long countAllActiveQr();

    @Query("""
    SELECT COUNT(q)
    FROM QrCode q
    WHERE q.business.businessId = :businessId
    AND q.active = true
""")
    Long countActiveQrByBusiness(@Param("businessId") Integer businessId);
}