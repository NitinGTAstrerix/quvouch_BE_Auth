package com.spring.jwt.controller;

import com.spring.jwt.dto.ClientDetailsDTO;
import com.spring.jwt.dto.ClientListDTO;
import com.spring.jwt.dto.SaleRepresentativeInfo;
import com.spring.jwt.dto.UserDTO;
import com.spring.jwt.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

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
    }}

