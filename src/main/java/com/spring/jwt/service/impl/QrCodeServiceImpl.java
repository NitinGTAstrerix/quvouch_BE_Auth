package com.spring.jwt.service.impl;

import com.spring.jwt.dto.AssignQrCodeRequest;
import com.spring.jwt.dto.QrCodeResponse;
import com.spring.jwt.dto.UserProfileDTO;
import com.spring.jwt.entity.Business;
import com.spring.jwt.entity.QrCode;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.BusinessNotFound;
import com.spring.jwt.mapper.QrCodeMapper;
import com.spring.jwt.repository.BusinessRepository;
import com.spring.jwt.repository.QrCodeRepository;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.service.QrCodeService;
import com.spring.jwt.service.UserService;
import com.spring.jwt.utils.QrCodeGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QrCodeServiceImpl implements QrCodeService {

        private final QrCodeRepository qrCodeRepository;
        private final QrCodeGenerator qrCodeGenerator;
        private final UserRepository userRepository;
        private final BusinessRepository businessRepository;
        private final QrCodeMapper qrCodeMapper;
        private final UserService userService;

        @Value("${app.qr.base-url}")
        private String baseUrl;

    @Override
    public byte[] createQrCode(Integer businessId) {

        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new BusinessNotFound("Business not found"));

        // 1. Generate unique QR ID
        String qrId = UUID.randomUUID().toString();

        // 2. Create QR link
        String qrLink = baseUrl + qrId;

        // 3. Generate QR image FIRST
        byte[] qrImageBytes = qrCodeGenerator.generateQrCode(qrLink, 300, 300);

        // 4. Save QR metadata + image in DB
        QrCode qrCode = new QrCode();
        qrCode.setId(qrId);
        qrCode.setQrLink(qrLink);
        qrCode.setBusiness(business);

        // 🔥 THIS LINE WAS MISSING
        qrCode.setQrImage(qrImageBytes);

        // 5. Save entity
        qrCodeRepository.save(qrCode);

        // 6. Return image
        return qrImageBytes;
    }


    @Override
    public List<QrCodeResponse> getAllQr() {

        List<QrCode> all = qrCodeRepository.findAll();
        return all.stream().map(qrCodeMapper::toQrCode).collect(Collectors.toList());
    }

    @Override
    public String EnableQrCode(String qrId) {
        QrCode qrCode = qrCodeRepository.findById(qrId).orElseThrow(() -> new RuntimeException("Qr Code not Found"));

        qrCode.setActive(true);
        qrCodeRepository.save(qrCode);
        return "Qr Code Enabled";
    }

    @Override
    public String DisableQrCode(String qrId) {
        QrCode qrCode = qrCodeRepository.findById(qrId).orElseThrow(() -> new RuntimeException("Qr Code not Found"));

        qrCode.setActive(false);
        qrCodeRepository.save(qrCode);
        return "Qr Code Disable";
    }

    @Override
    public byte[] downloadQrCode(String qrId) {

        QrCode qrCode = qrCodeRepository.findById(qrId)
                .orElseThrow(() -> new RuntimeException("QR Code not found"));

        if (qrCode.getQrImage() == null || qrCode.getQrImage().length == 0) {
            throw new RuntimeException("QR image not found in database");
        }

        return qrCode.getQrImage();
    }

    @Override
    @Transactional
    public void assignQrToBusiness(AssignQrCodeRequest request) {

        QrCode qrCode = qrCodeRepository.findById(request.getQrCodeId())
                .orElseThrow(() -> new RuntimeException("QR Code not found"));

        if (qrCode.getStatus() != QrCode.QrStatus.UNASSIGNED) {
            throw new RuntimeException("QR Code already assigned");
        }

        Business business = businessRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Business not found"));

        // ✅ Correct way using your DTO structure
        UserProfileDTO profile = userService.getCurrentUserProfile();

        String userIdStr = profile.getUser().getUserId();

        Long salesUserId = Long.parseLong(userIdStr);

        User salesUser = userRepository.findById(salesUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        qrCode.setBusiness(business);
        qrCode.setAssignedBy(salesUser);
        qrCode.setAssignedAt(LocalDateTime.now());
        qrCode.setLocation(request.getLocationLabel());
        qrCode.setStatus(QrCode.QrStatus.ASSIGNED);

        qrCodeRepository.save(qrCode);
    }

    @Override
    public List<QrCode> getUnassignedQrCodes() {

        return qrCodeRepository.findByStatus(QrCode.QrStatus.UNASSIGNED);
    }

    @Override
    public List<QrCode> getMyAssignedQrCodes() {

        UserProfileDTO profile = userService.getCurrentUserProfile();

        String userIdStr = profile.getUser().getUserId();

        Long salesUserId = Long.parseLong(userIdStr);

        User salesUser = userRepository.findById(salesUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return qrCodeRepository.findByAssignedBy(salesUser);
    }
}