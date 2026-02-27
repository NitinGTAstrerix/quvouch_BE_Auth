package com.spring.jwt.controller;

import com.spring.jwt.dto.FeedbackResponseDto;
import com.spring.jwt.dto.SaleRepresentativeInfo;
import com.spring.jwt.dto.UserDTO;
import com.spring.jwt.dto.*;
import com.spring.jwt.entity.Review;
import com.spring.jwt.service.AdminService;
import com.spring.jwt.service.FeedbackService;
import com.spring.jwt.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final ReviewService reviewService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/sale-representatives")
    public ResponseEntity<List<SaleRepresentativeInfo>> getSaleReps() {
        return ResponseEntity.ok(adminService.getAllSaleRepsWithClientCount());
    }

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


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/clients")
    public ResponseEntity<List<ClientListDTO>> getAllClients() {
        return ResponseEntity.ok(adminService.getAllClients());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/clients/{businessId}")
    public ResponseEntity<ClientDetailsDTO> getClientDetails(
            @PathVariable Integer businessId) {

        return ResponseEntity.ok(
                adminService.getClientDetails(businessId)
        );
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/qrcodes")
    public ResponseEntity<List<AdminQrCodeDTO>> getAllQrCodes() {

        return ResponseEntity.ok(
                adminService.getAllQrCodes()
        );
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/dashboard/recent-reviews")
    public ResponseEntity<List<AdminRecentReviewDto>> getRecentReviews() {
        return ResponseEntity.ok(adminService.getRecentReviews());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/qrcodes/{id}/download")
    public ResponseEntity<byte[]> downloadQrCode(@PathVariable String id) {

        byte[] image = adminService.downloadQrCode(id);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=qr-" + id + ".png")
                .header("Content-Type", "image/png")
                .body(image);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/qrcodes/{id}/link")
    public ResponseEntity<String> getQrCodeLink(@PathVariable String id) {

        return ResponseEntity.ok(adminService.getQrCodeLink(id));
    }
}