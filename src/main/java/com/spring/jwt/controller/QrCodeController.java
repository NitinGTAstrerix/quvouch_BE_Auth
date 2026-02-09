package com.spring.jwt.controller;

import com.spring.jwt.dto.QrCodeResponse;
import com.spring.jwt.entity.QrCode;
import com.spring.jwt.service.QrCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Operation(summary = "Generate QR Code for a Business", description = "Generates a QR code image for the given business ID. ")
    @PostMapping("/generate/{businessId}")
    public ResponseEntity<byte[]> generateQrCode(@PathVariable Integer businessId) {

        byte[] qrImage = qrCodeService.createQrCode(businessId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=qrcode.png")
                .contentType(MediaType.IMAGE_PNG)
                .body(qrImage);
    }

    @Operation(summary = "Get all QR Codes", description = "Fetches all QR codes in the system. " + "This API is accessible only by ADMIN users.")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<QrCodeResponse>> getAllQrcodes()
    {
        List<QrCodeResponse> allQr = qrCodeService.getAllQr();
        return ResponseEntity.ok(allQr);
    }
    @Operation(summary = "Enable a QR Code",description = "Enables a disabled QR code. "+ "Only ADMIN and SALE_REPRESENTATIVE roles are allowed.")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyAuthority('ADMIN','SALE_REPRESENTATIVE')")
    @PutMapping("/{qrId}/enable")
    public ResponseEntity<String> handelQrEnable(@PathVariable String qrId)
    {
        String s = qrCodeService.EnableQrCode(qrId);
        return ResponseEntity.ok(s);
    }
    @Operation(summary = "Disable a QR Code",description = "Enables a disabled QR code. "+ "Only ADMIN and SALE_REPRESENTATIVE roles are allowed.")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyAuthority('ADMIN','SALE_REPRESENTATIVE')")
    @PutMapping("/{qrId}/disable")
    public ResponseEntity<String> handelQrDisable(@PathVariable String qrId)
    {
        String s = qrCodeService.DisableQrCode(qrId);
        return ResponseEntity.ok(s);
    }
}
