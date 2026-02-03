package com.spring.jwt.controller;

import com.spring.jwt.dto.BusinessRequestDto;
import com.spring.jwt.dto.BusinessResponseDto;
import com.spring.jwt.dto.QrCodeResponse;
import com.spring.jwt.dto.SalesDashboardDto;
import com.spring.jwt.entity.Business;
import com.spring.jwt.service.SalesClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sales/clients")
@RequiredArgsConstructor
@Tag(name = "Sales Client Controller", description = "Endpoints for managing clients, searching businesses, and handling sales representative operations")
public class SalesClientController {

    private final SalesClientService salesClientService;

    @Operation(summary = "Create a new client")
    @PreAuthorize("hasAnyRole('SALE_REPRESENTATIVE', 'ADMIN')")
    @PostMapping
    public ResponseEntity<BusinessResponseDto> createClient(@RequestBody @Valid BusinessRequestDto dto) {
        BusinessResponseDto response = salesClientService.createClient(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get all clients of the logged-in sales representative")
    @PreAuthorize("hasAnyRole('SALE_REPRESENTATIVE', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<BusinessResponseDto>> getMyClients() {
        List<BusinessResponseDto> clients = salesClientService.getMyClients();
        return ResponseEntity.ok(clients);
    }

    @Operation(summary = "Get details of a single client by ID")
    @PreAuthorize("hasAnyRole('SALE_REPRESENTATIVE', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<BusinessResponseDto> getClient(@PathVariable Integer id) {
        return ResponseEntity.ok(salesClientService.getClientById(id));
    }

    @Operation(summary = "Search clients by business name keyword")
    @PreAuthorize("hasAnyRole('SALE_REPRESENTATIVE', 'ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<List<BusinessResponseDto>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(salesClientService.searchClients(keyword));
    }

    @Operation(summary = "Update an existing client")
    @PreAuthorize("hasAnyRole('SALE_REPRESENTATIVE', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<BusinessResponseDto> updateClient(@PathVariable Integer id,
                                                            @RequestBody @Valid BusinessRequestDto dto) {
        return ResponseEntity.ok(salesClientService.updateClient(id, dto));
    }

    @Operation(summary = "Toggle client status: PENDING → ACTIVE → INACTIVE")
    @PreAuthorize("hasAnyRole('SALE_REPRESENTATIVE', 'ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> changeStatus(@PathVariable Integer id) {
        salesClientService.changeStatus(id);
        return ResponseEntity.ok("Status Updated Successfully");
    }

    @Operation(summary = "Delete a client by ID")
    @PreAuthorize("hasAnyRole('SALE_REPRESENTATIVE', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Integer id) {
        salesClientService.deleteClient(id);
        return ResponseEntity.ok("Client Deleted Successfully");
    }

    @Operation(summary = "Get dashboard summary for the logged-in sales representative")
    @PreAuthorize("hasAnyRole('SALE_REPRESENTATIVE', 'ADMIN')")
    @GetMapping("/dashboard")
    public ResponseEntity<SalesDashboardDto> getDashboard() {
        return ResponseEntity.ok(salesClientService.getDashboardData());
    }

    @Operation(summary = "Get clients filtered by status")
    @PreAuthorize("hasAnyRole('SALE_REPRESENTATIVE', 'ADMIN')")
    @GetMapping("/status")
    public ResponseEntity<List<BusinessResponseDto>> getByStatus(@RequestParam Business.BusinessStatus value) {
        return ResponseEntity.ok(salesClientService.getClientsByStatus(value));
    }

    @Operation(summary = "Get all active QR codes of the logged-in user's clients")
    @PreAuthorize("hasAnyRole('SALE_REPRESENTATIVE', 'ADMIN')")
    @GetMapping("/qr/active")
    public ResponseEntity<List<QrCodeResponse>> getActiveQrs() {
        return ResponseEntity.ok(salesClientService.getActiveQrCodes());
    }
}
