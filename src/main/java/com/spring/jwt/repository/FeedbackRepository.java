<<<<<<< HEAD
package com.app.quvouch.repository;

import com.app.quvouch.Models.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {
=======
package com.spring.jwt.repository;

import com.spring.jwt.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    boolean existsByEmailAndBusinessId(String email, Long businessId);
>>>>>>> fec191664a493331f12a1e1fed807664e99cd9c4
}
