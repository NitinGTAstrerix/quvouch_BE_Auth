package com.spring.jwt.service.impl;

import com.spring.jwt.dto.SettingsProfileDTO;
import com.spring.jwt.dto.UpdateProfileDTO;
import com.spring.jwt.dto.ChangePasswordDTO;
import com.spring.jwt.entity.User;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.service.SettingsService;
import com.spring.jwt.service.security.UserDetailsCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettingsServiceImpl implements SettingsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        return user;
    }
    @Override
    public SettingsProfileDTO getProfile() {

        User user = getCurrentUser();

        SettingsProfileDTO dto = new SettingsProfileDTO();

        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setMobileNumber(String.valueOf(user.getMobileNumber()));
        dto.setAddress(user.getAddress());

        dto.setRole(
                user.getRoles().stream()
                        .findFirst()
                        .map(role -> role.getName())
                        .orElse("USER")
        );

        return dto;
    }

    @Override
    public void updateProfile(UpdateProfileDTO request) {

        User user = getCurrentUser();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setMobileNumber(Long.valueOf(request.getMobileNumber()));
        user.setAddress(request.getAddress());

        userRepository.save(user);
    }

    @Override
    public void changePassword(ChangePasswordDTO request) {

        User user = getCurrentUser();

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("User not found");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);
    }
}