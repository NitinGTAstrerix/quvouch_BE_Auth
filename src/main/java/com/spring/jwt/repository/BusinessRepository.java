package com.spring.jwt.repository;

import com.spring.jwt.dto.ClientDetailsDTO;
import com.spring.jwt.dto.ClientListDTO;
import com.spring.jwt.entity.Business;
import com.spring.jwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business, Integer> {

    Optional<Business> findByUser_Id(Integer Id);

    List<Business> findByUser(User user);

    Optional<Business> findByBusinessIdAndUser(Integer id, User user);

    List<Business> findByUserAndBusinessNameContainingIgnoreCase(
            User user,
            String keyword
    );

    long countByUser(User user);

    long countByUserAndStatus(User user, Business.BusinessStatus status);

    List<Business> findByUserAndStatus(User user, Business.BusinessStatus status);

    @Query("""
SELECT new com.spring.jwt.dto.ClientListDTO(
    b.businessId,
    b.businessName,
    b.businessType,
    b.phoneNumber,
    b.businessEmail,
    b.status,
    b.createdAt,
    COUNT(DISTINCT q),
    COUNT(DISTINCT r)
)
FROM Business b
LEFT JOIN b.qrCode q
LEFT JOIN Review r ON r.business.businessId = b.businessId
GROUP BY b.businessId
""")
    List<ClientListDTO> getAllClients();

    List<Business> findByUser_IdIn(List<Integer> userIds);

    @Query("""
SELECT new com.spring.jwt.dto.ClientDetailsDTO(
    b.businessId,
    b.businessName,
    b.businessType,
    b.address,
    b.phoneNumber,
    b.businessEmail,
    b.status,
    b.createdAt,
    COUNT(DISTINCT q),
    COUNT(DISTINCT r),
    AVG(r.rating)
)
FROM Business b
LEFT JOIN b.qrCode q
LEFT JOIN Review r ON r.business.businessId = b.businessId
WHERE b.businessId = :businessId
GROUP BY b.businessId
""")
    ClientDetailsDTO getClientDetails(@Param("businessId") Integer businessId);

}
