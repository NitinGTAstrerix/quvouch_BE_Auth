package com.spring.jwt.service.impl;

import com.spring.jwt.dto.NotificationSettingsDTO;
import com.spring.jwt.entity.NotificationSettings;
import com.spring.jwt.entity.User;
import com.spring.jwt.repository.NotificationSettingsRepository;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.service.NotificationSettingsService;
import com.spring.jwt.service.security.UserDetailsCustom;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationSettingsServiceImpl implements NotificationSettingsService {

    private final NotificationSettingsRepository repository;
    private final UserRepository userRepository;

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserDetailsCustom userDetails =
                (UserDetailsCustom) authentication.getPrincipal();

        return userRepository.findByEmail(userDetails.getUsername());
    }

    @Override
    public NotificationSettingsDTO getSettings() {

        User user = getCurrentUser();

        Long userId = user.getId().longValue();

        NotificationSettings settings =
                repository.findByUserId(userId)
                        .orElse(new NotificationSettings());

        NotificationSettingsDTO dto = new NotificationSettingsDTO();

        dto.setEmailNotifications(settings.isEmailNotifications());
        dto.setSmsNotifications(settings.isSmsNotifications());
        dto.setClientAssigned(settings.isClientAssigned());
        dto.setDealUpdates(settings.isDealUpdates());
        dto.setSystemAlerts(settings.isSystemAlerts());

        return dto;
    }

    @Override
    public void updateSettings(NotificationSettingsDTO request) {

        User user = getCurrentUser();

        Long userId = user.getId().longValue();

        NotificationSettings settings =
                repository.findByUserId(userId)
                        .orElse(new NotificationSettings());

        settings.setUserId(userId);
        settings.setEmailNotifications(request.getEmailNotifications());
        settings.setSmsNotifications(request.getSmsNotifications());
        settings.setClientAssigned(request.getClientAssigned());
        settings.setDealUpdates(request.getDealUpdates());
        settings.setSystemAlerts(request.getSystemAlerts());

        repository.save(settings);
    }
}