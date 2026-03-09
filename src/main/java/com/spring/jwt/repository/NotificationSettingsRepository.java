package com.spring.jwt.repository;

import com.spring.jwt.entity.NotificationSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationSettingsRepository
        extends JpaRepository<NotificationSettings, Long> {

    Optional<NotificationSettings> findByUserId(Long userId);
}
