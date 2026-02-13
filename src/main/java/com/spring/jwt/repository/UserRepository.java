package com.spring.jwt.repository;

import com.spring.jwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

<<<<<<< HEAD
=======
import java.util.List;
>>>>>>> fec191664a493331f12a1e1fed807664e99cd9c4
import java.util.Map;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);


    User findByResetPasswordToken(String token);

    Optional<User> findByMobileNumber(@Param("mobileNumber") Long mobileNumber);
    
    @Query(value = "SELECT * FROM users WHERE user_id = :id", nativeQuery = true)
    Map<String, Object> findRawUserById(@Param("id") Long id);
<<<<<<< HEAD
=======

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = 'SALE_REPRESENTATIVE'")
    List<User> findAllSaleRepresentatives();

    List<User> findBySaleRepresentative_Id(Integer saleRepId);

    Long countBySaleRepresentative_Id(Integer saleRepId);



>>>>>>> fec191664a493331f12a1e1fed807664e99cd9c4
}
