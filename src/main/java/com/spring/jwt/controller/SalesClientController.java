package com.spring.jwt.controller;

import com.spring.jwt.dto.*;
import com.spring.jwt.entity.Business;
import com.spring.jwt.entity.QrCode;
import com.spring.jwt.entity.User;
import com.spring.jwt.repository.QrCodeRepository;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.service.BusinessService;
import com.spring.jwt.service.QrCodeService;
import com.spring.jwt.service.SalesClientService;
import com.spring.jwt.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/sales/clients")
@RequiredArgsConstructor
@Tag(name = "Sales Client Controller", description = "Endpoints for managing clients, searching businesses, and handling sales representative operations")
public class SalesClientController {

    private final SalesClientService salesClientService;
    private final UserService userService;
    private final BusinessService businessService;
    private final QrCodeRepository qrCodeRepository;
    private final UserRepository  userRepository;

    // NEW API → Get current logged-in Sales Representative profile
    @Operation(summary = "Get current logged-in sales representative profile")
    @PreAuthorize("hasAnyAuthority('SALE_REPRESENTATIVE', 'ADMIN')")
    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> getCurrentSalesProfile() {
        UserProfileDTO profile = userService.getCurrentUserProfile();
        return ResponseEntity.ok(profile);
    }

    @Operation(summary = "Create a new client")
    @PreAuthorize("hasAnyAuthority('SALE_REPRESENTATIVE', 'ADMIN')")
    @PostMapping
    public ResponseEntity<BusinessResponseDto> createClient(@RequestBody @Valid BusinessRequestDto dto) {
        BusinessResponseDto response = salesClientService.createClient(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get all clients of the logged-in sales representative")
    @PreAuthorize("hasAnyAuthority('SALE_REPRESENTATIVE', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<BusinessResponseDto>> getMyClients() {
        List<BusinessResponseDto> clients = salesClientService.getMyClients();
        return ResponseEntity.ok(clients);
    }

    @Operation(summary = "Get details of a single client by ID")
    @PreAuthorize("hasAnyAuthority('SALE_REPRESENTATIVE', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<BusinessResponseDto> getClient(@PathVariable Integer id) {
        return ResponseEntity.ok(salesClientService.getClientById(id));
    }

    @Operation(summary = "Search clients by business name keyword")
    @PreAuthorize("hasAnyAuthority('SALE_REPRESENTATIVE', 'ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<List<BusinessResponseDto>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(salesClientService.searchClients(keyword));
    }

    @Operation(summary = "Update an existing client")
    @PreAuthorize("hasAnyAuthority('SALE_REPRESENTATIVE', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<BusinessResponseDto> updateClient(
            @PathVariable Integer id,
            @RequestBody @Valid BusinessRequestDto dto) {

        return ResponseEntity.ok(salesClientService.updateClient(id, dto));
    }

    @Operation(summary = "Toggle business status: ACTIVE → INACTIVE")
    @PreAuthorize("hasAnyAuthority('SALE_REPRESENTATIVE', 'ADMIN')")
    @PatchMapping("/business/{id}/status/toggle")
    public ResponseEntity<?> toggleBusinessStatus(@PathVariable Integer id) {

        Business.BusinessStatus status =
                salesClientService.changeStatus(id);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Business status updated successfully",
                        "businessId", id,
                        "status", status
                )
        );
    }

    @Operation(summary = "Delete a client by ID")
    @PreAuthorize("hasAnyAuthority('SALE_REPRESENTATIVE', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Integer id) {
        salesClientService.deleteClient(id);
        return ResponseEntity.ok("Client Deleted Successfully");
    }

    @Operation(summary = "Get dashboard summary for the logged-in sales representative")
    @PreAuthorize("hasAnyAuthority('SALE_REPRESENTATIVE', 'ADMIN')")
    @GetMapping("/dashboard")
    public ResponseEntity<SalesDashboardDto> getDashboard() {
        return ResponseEntity.ok(salesClientService.getDashboardData());
    }

    @Operation(summary = "Get clients filtered by status")
    @PreAuthorize("hasAnyAuthority('SALE_REPRESENTATIVE', 'ADMIN')")
    @GetMapping("/status")
    public ResponseEntity<List<BusinessResponseDto>> getByStatus(
            @RequestParam Business.BusinessStatus value) {

        return ResponseEntity.ok(salesClientService.getClientsByStatus(value));
    }

    @Operation(summary = "Get all active QR codes of the logged-in user's clients")
    @PreAuthorize("hasAnyAuthority('SALE_REPRESENTATIVE', 'ADMIN')")
    @GetMapping("/qr/active")
    public ResponseEntity<List<QrCodeResponse>> getActiveQrs() {
        return ResponseEntity.ok(salesClientService.getActiveQrCodes());
    }

    @PreAuthorize("hasAnyAuthority('SALE_REPRESENTATIVE','ADMIN')")
    @GetMapping("/qrcodes/my")
    public ResponseEntity<List<QrCode>> getMyAssignedQrCodes() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User loggedUser;

        if (principal instanceof UserDetails userDetails) {
            loggedUser = userRepository.findByEmail(userDetails.getUsername()); // findByEmail returns User
            if (loggedUser == null) {
                throw new RuntimeException("User not found");
            }
        } else {
            throw new RuntimeException("User not authenticated");
        }

        List<QrCode> qrCodes = qrCodeRepository.findByAssignedBy(loggedUser).stream()
                .filter(q -> q.getStatus() == QrCode.QrStatus.ACTIVE)
                .toList();

        return ResponseEntity.ok(qrCodes);
    }

    @Operation(summary = "Get all clients registered by logged-in Sales Representative")
    @PreAuthorize("hasAuthority('SALE_REPRESENTATIVE')")
    @GetMapping("/registered-users")
    public ResponseEntity<List<ClientResponseDto>> getMyRegisteredUsers() {

        return ResponseEntity.ok(
                salesClientService.getMyRegisteredClients()
        );
    }


    @DeleteMapping("/business/{businessId}")
    public ResponseEntity<String> deleteBusiness(
            @PathVariable Integer businessId) {

        String response = businessService.deleteBusiness(businessId);

        return ResponseEntity.ok(response);
    }
}