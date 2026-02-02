package com.spring.jwt.controller;

import com.spring.jwt.dto.QrCodeResponse;
import com.spring.jwt.entity.QrCode;
import com.spring.jwt.service.QrCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/qrcode")
@RequiredArgsConstructor
public class QrCodeController {

    private final QrCodeService qrCodeService;

    @GetMapping("/generate/{businessId}")
    public ResponseEntity<byte[]> generateQrCode(@PathVariable Integer businessId) {

        byte[] qrImage = qrCodeService.createQrCode(businessId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=qrcode.png")
                .contentType(MediaType.IMAGE_PNG)
                .body(qrImage);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<QrCodeResponse>> getAllQrcodes()
    {
        List<QrCodeResponse> allQr = qrCodeService.getAllQr();
        return ResponseEntity.ok(allQr);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SALE_REPRESENTATIVE')")
    @PutMapping("/{qrId}/enable")
    public ResponseEntity<String> handelQrEnable(@PathVariable String qrId)
    {
        String s = qrCodeService.EnableQrCode(qrId);
        return ResponseEntity.ok(s);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SALE_REPRESENTATIVE')")
    @PutMapping("/{qrId}/disable")
    public ResponseEntity<String> handelQrDisable(@PathVariable String qrId)
    {
        String s = qrCodeService.DisableQrCode(qrId);
        return ResponseEntity.ok(s);
    }
}
