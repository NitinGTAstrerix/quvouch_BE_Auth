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

        Object principal = authentication.getPrincipal();

        String email;

        if (principal instanceof UserDetailsCustom) {
            email = ((UserDetailsCustom) principal).getUsername();
        } else {
            email = principal.toString();
        }

        return userRepository.findByEmail(email);
    }

    @Override
    public NotificationSettingsDTO getSettings() {

        User user = getCurrentUser();
        Long userId = user.getId().longValue();

        NotificationSettings settings = repository
                .findByUserId(userId)
                .orElseGet(() -> {
                    NotificationSettings defaultSettings = new NotificationSettings();
                    defaultSettings.setUserId(userId);
                    defaultSettings.setEmailNotifications(true);
                    defaultSettings.setSmsNotifications(false);
                    defaultSettings.setClientAssigned(true);
                    defaultSettings.setDealUpdates(true);
                    defaultSettings.setSystemAlerts(true);
                    return repository.save(defaultSettings);
                });

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

        NotificationSettings settings = repository
                .findByUserId(userId)
                .orElseGet(() -> {
                    NotificationSettings newSettings = new NotificationSettings();
                    newSettings.setUserId(userId);
                    return newSettings;
                });

        settings.setEmailNotifications(request.getEmailNotifications());
        settings.setSmsNotifications(request.getSmsNotifications());
        settings.setClientAssigned(request.getClientAssigned());
        settings.setDealUpdates(request.getDealUpdates());
        settings.setSystemAlerts(request.getSystemAlerts());

        repository.save(settings);
    }
}