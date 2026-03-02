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
    public byte[] createQrCode(Integer businessId, String location) {

        // 🔒 Validate location
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location is required");
        }

        // 1️⃣ Fetch business
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new BusinessNotFound("Business not found"));

        // 2️⃣ Prevent duplicate location QR
        boolean exists = qrCodeRepository
                .existsByBusinessAndLocationIgnoreCase(business, location.trim());

        if (exists) {
            throw new RuntimeException("QR already exists for this location");
        }

        // 3️⃣ Generate unique QR ID
        String qrId = UUID.randomUUID().toString();

        // 4️⃣ Create QR link
        String qrLink = baseUrl + qrId;

        // 5️⃣ Generate QR image
        byte[] qrImageBytes = qrCodeGenerator.generateQrCode(qrLink, 300, 300);

        // 6️⃣ Save QR metadata
        QrCode qrCode = new QrCode();
        qrCode.setId(qrId);
        qrCode.setQrLink(qrLink);
        qrCode.setBusiness(business);
        qrCode.setLocation(location.trim());
        qrCode.setQrImage(qrImageBytes);
        qrCode.setStatus(QrCode.QrStatus.ACTIVE);

        qrCodeRepository.save(qrCode);

        // 7️⃣ Return image
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
}