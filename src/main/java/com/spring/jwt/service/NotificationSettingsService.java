package com.spring.jwt.service;

import com.spring.jwt.dto.NotificationSettingsDTO;

public interface NotificationSettingsService {

    NotificationSettingsDTO getSettings();

    void updateSettings(NotificationSettingsDTO request);
}