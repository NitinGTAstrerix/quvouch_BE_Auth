package com.spring.jwt.service.impl;

import com.spring.jwt.dto.QrCodeResponse;
import com.spring.jwt.entity.Business;
import com.spring.jwt.entity.QrCode;
import com.spring.jwt.exception.BusinessNotFound;
import com.spring.jwt.mapper.QrCodeMapper;
import com.spring.jwt.repository.BusinessRepository;
import com.spring.jwt.repository.QrCodeRepository;
import com.spring.jwt.service.QrCodeService;
import com.spring.jwt.utils.QrCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QrCodeServiceImpl implements QrCodeService {

        private final QrCodeRepository qrCodeRepository;
        private final QrCodeGenerator qrCodeGenerator;
        private final BusinessRepository businessRepository;
        private final QrCodeMapper qrCodeMapper;

        @Value("${app.qr.base-url}")
        private String baseUrl;

        @Override
        public byte[] createQrCode(Integer businessId) {

            Business business = businessRepository.findById(businessId).orElseThrow(() -> new BusinessNotFound("Business not found"));
            // 1. Generate unique QR ID
            String qrId = UUID.randomUUID().toString();

            // 2. Create QR link
            String qrLink = baseUrl + qrId;

            // 3. Save QR metadata in DB
            QrCode qrCode = new QrCode();
            qrCode.setId(qrId);
            qrCode.setQrLink(qrLink);
            qrCode.setBusiness(business);
            qrCodeRepository.save(qrCode);

            // 4. Generate QR image
            return qrCodeGenerator.generateQrCode(qrLink, 300, 300);
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

}
