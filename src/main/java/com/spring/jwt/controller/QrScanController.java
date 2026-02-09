package com.spring.jwt.controller;

import com.spring.jwt.entity.QrCode;
import com.spring.jwt.repository.QrCodeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scan/qr")
@RequiredArgsConstructor
@Tag(name = "Qr code scan controller" , description = "this controller is used to scan the qr for the business and give them review")
public class QrScanController {

    private final QrCodeRepository qrCodeRepository;

    @Operation(summary = "Scan qr by customer",description = "take the review for the business")
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
