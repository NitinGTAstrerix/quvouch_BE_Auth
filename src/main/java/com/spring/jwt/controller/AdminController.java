package com.spring.jwt.controller;

import com.spring.jwt.dto.FeedbackResponseDto;
import com.spring.jwt.dto.SaleRepresentativeInfo;
import com.spring.jwt.dto.UserDTO;
import com.spring.jwt.dto.*;
import com.spring.jwt.entity.Review;
import com.spring.jwt.service.AdminService;
import com.spring.jwt.service.FeedbackService;
import com.spring.jwt.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final ReviewService reviewService;

    @Operation(summary = "Get All Sales Representative", description = "Detailed List of All Sales Representative")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/sale-representatives")
    public ResponseEntity<List<SaleRepresentativeInfo>> getSaleReps() {
        return ResponseEntity.ok(adminService.getAllSaleRepsWithClientCount());
    }

    @Operation(summary = "Get Sales Representative by His ID", description = "Details of Single Sales Representative using his ID")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/sale-representatives/{id}")
    public ResponseEntity<UserDTO> getSaleRepById(@PathVariable Long id) {

        return ResponseEntity.ok(
                adminService.getSaleRepresentativeById(id)
        );
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/reviews/salerep/{saleRepId}")
    public ResponseEntity<List<FeedbackResponseDto>> getReviewsBySaleRep(
            @PathVariable Integer saleRepId) {

        return ResponseEntity.ok(
                reviewService.getReviewsBySaleRep(saleRepId)
        );
    }


    @Operation(summary = "Get All Clients", description = "Detailed List of All Clients")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/clients")
    public ResponseEntity<List<ClientListDTO>> getAllClients() {
        return ResponseEntity.ok(adminService.getAllClients());
    }

    @Operation(summary = "Get Business of a Client using Business ID", description = "Details of Single Business using his ID")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/clients/{businessId}")
    public ResponseEntity<ClientDetailsDTO> getClientDetails(
            @PathVariable Integer businessId) {

        return ResponseEntity.ok(
                adminService.getClientDetails(businessId)
        );
    }

    @Operation(summary = "Get all QR Codes", description = "Details of all QR Codes of All Business")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/qrcodes")
    public ResponseEntity<List<AdminQrCodeDTO>> getAllQrCodes() {

        return ResponseEntity.ok(
                adminService.getAllQrCodes()
        );
    }

    @Operation(summary = "Get Recent Reviews", description = "Details All Recent Reviews for all Business")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/dashboard/recent-reviews")
    public ResponseEntity<List<AdminRecentReviewDto>> getRecentReviews() {
        return ResponseEntity.ok(adminService.getRecentReviews());
    }

    @Operation(summary = "Download QR Code of a Business using Business ID", description = "Download QR Code of Any Business")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/qrcodes/{id}/download")
    public ResponseEntity<byte[]> downloadQrCode(@PathVariable String id) {

        byte[] image = adminService.downloadQrCode(id);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=qr-" + id + ".png")
                .header("Content-Type", "image/png")
                .body(image);
    }

    @Operation(summary = "Get QR Code Link using QR Code ID", description = "Generating QR Code Link for Sharing")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/qrcodes/{id}/link")
    public ResponseEntity<String> getQrCodeLink(@PathVariable String id) {

        return ResponseEntity.ok(adminService.getQrCodeLink(id));
    }

    @Operation(summary = "Admin dashboard statistics")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardDTO> getDashboardStats() {

        AdminDashboardDTO stats = adminService.getDashboardStats();

        return ResponseEntity.ok(stats);
    }
}