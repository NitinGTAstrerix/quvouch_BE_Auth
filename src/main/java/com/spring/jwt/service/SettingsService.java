package com.spring.jwt.service;

import com.spring.jwt.dto.SettingsProfileDTO;
import com.spring.jwt.dto.UpdateProfileDTO;
import com.spring.jwt.dto.ChangePasswordDTO;

public interface SettingsService {

    SettingsProfileDTO getProfile();

    void updateProfile(UpdateProfileDTO request);

    void changePassword(ChangePasswordDTO request);
}