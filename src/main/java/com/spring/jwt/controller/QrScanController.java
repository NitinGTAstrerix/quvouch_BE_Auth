package com.spring.jwt.controller;

import com.spring.jwt.entity.QrCode;
import com.spring.jwt.repository.QrCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scan/qr")
@RequiredArgsConstructor
public class QrScanController {

    private final QrCodeRepository qrCodeRepository;

    @GetMapping("/{qrId}")
    public ResponseEntity<String> scanQr(@PathVariable String qrId) {

        QrCode qrCode = qrCodeRepository.findById(qrId)
                .orElseThrow(() -> new RuntimeException("Invalid QR Code"));

        if (!qrCode.isActive()) {
            return ResponseEntity.badRequest().body("QR Code is inactive");
        }

        qrCode.setScanCount(qrCode.getScanCount() + 1);
        qrCodeRepository.save(qrCode);

        return ResponseEntity.ok("QR scanned successfully");
    }
}
