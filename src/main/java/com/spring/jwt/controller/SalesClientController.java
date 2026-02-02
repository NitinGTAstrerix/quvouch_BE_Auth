package com.spring.jwt.controller;

import com.spring.jwt.dto.BusinessRequestDto;
import com.spring.jwt.dto.BusinessResponseDto;
import com.spring.jwt.dto.QrCodeResponseDto;
import com.spring.jwt.dto.SalesDashboardDto;
import com.spring.jwt.entity.BusinessStatus;
import com.spring.jwt.service.SalesClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sales/clients")
@RequiredArgsConstructor
@Tag(name = "Sales Client Controller",description = "Endpoints for managing clients, searching businesses, and handling sales representative operations")
public class SalesClientController {

    private final SalesClientService salesClientService;

    // Create Client
    @PostMapping
    public ResponseEntity<BusinessResponseDto> createClient(@RequestBody @Valid BusinessRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(salesClientService.createClient(dto));
    }

    // Get My Clients
    @GetMapping
    public ResponseEntity<List<BusinessResponseDto>> getMyClients() {

        return ResponseEntity.ok(
                salesClientService.getMyClients()
        );
    }

    // Get Single Client
    @GetMapping("/{id}")
    public ResponseEntity<BusinessResponseDto> getClient(@PathVariable Integer id) {
        return ResponseEntity.ok(
                salesClientService.getClientById(id)
        );
    }

    // Search Clients
    @GetMapping("/search")
    public ResponseEntity<List<BusinessResponseDto>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(
                salesClientService.searchClients(keyword)
        );
    }

    // Update Client
    @PutMapping("/{id}")
    public ResponseEntity<BusinessResponseDto> update( @PathVariable Integer id,@RequestBody @Valid BusinessRequestDto dto) {
        return ResponseEntity.ok(
                salesClientService.updateClient(id, dto)
        );
    }

    // Activate / Deactivate
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> changeStatus(@PathVariable Integer id) {
        salesClientService.changeStatus(id);

        return ResponseEntity.ok("Status Updated");
    }

    @Operation(summary = "Get dashboard summary for sales rep")
    @GetMapping("/dashboard")
    public ResponseEntity<SalesDashboardDto> getDashboard() {

        return ResponseEntity.ok(
                salesClientService.getDashboardData()
        );
    }

    @GetMapping("/status")
    public ResponseEntity<List<BusinessResponseDto>> getByStatus(@RequestParam BusinessStatus value) {

        return ResponseEntity.ok(salesClientService.getClientsByStatus(value));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Integer id) {

        salesClientService.deleteClient(id);

        return ResponseEntity.ok("Client Deleted Successfully");
    }

    // Get Active QR Codes
    @GetMapping("/qr/active")
    public ResponseEntity<List<QrCodeResponseDto>> getActiveQrs() {

        return ResponseEntity.ok(
                salesClientService.getActiveQrCodes());
    }

}
