package com.spring.jwt.controller;

import com.spring.jwt.dto.BusinessDashboardDto;
import com.spring.jwt.dto.BusinessRequestDto;
import com.spring.jwt.dto.BusinessResponseDto;
import com.spring.jwt.mapper.BusinessMapper;
import com.spring.jwt.service.BusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/business")
@Tag(name = "Business controller its for client")
public class BusinessController {

    private final BusinessService businessService;
    private final BusinessMapper businessMapper;

    @Operation(summary = "create business apis", description = "This is create the business apis ")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyAuthority('ADMIN','SALE_REPRESENTATIVE')")
    @PostMapping
    public ResponseEntity<BusinessResponseDto> creteBusiness(@Valid @RequestBody BusinessRequestDto businessRequestDto)
    {
        BusinessResponseDto business = businessService.createBusiness(businessRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(business);
    }

    @Operation(summary = "get api by id", description = "This is create the business apis ")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyAuthority('ADMIN','SALE_REPRESENTATIVE')")
    @GetMapping("{businessId}")
    public ResponseEntity<BusinessResponseDto> getBusinessById(@PathVariable Integer businessId)
    {
        BusinessResponseDto business = businessService.getBusinessById(businessId);
        return ResponseEntity.ok(business);
    }

    @Operation(summary = "own business apis", description = "get own the business ")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/own")
    public ResponseEntity<BusinessResponseDto> getBusinessOwn()
    {
        BusinessResponseDto businessByOwn = businessService.getBusinessByOwn();
        return ResponseEntity.ok(businessByOwn);
    }

    @Operation(summary = "all  businesses ", description = "admin get all business")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyAuthority('ADMIN','SALE_REPRESENTATIVE')")
    @GetMapping
    public ResponseEntity<List<BusinessResponseDto>> getBusinesses()
    {
        List<BusinessResponseDto> allBusiness = businessService.getAllBusiness();
        return ResponseEntity.ok(allBusiness);
    }

    @Operation(summary = "get business by pagination", description = "get business by pages ")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyAuthority('ADMIN','SALE_REPRESENTATIVE')")
    @GetMapping("/page")
    public ResponseEntity<Page<BusinessResponseDto>> getBusinessByPageNumber(@RequestParam int pageNo, @RequestParam int pageSize)
    {
        Page<BusinessResponseDto> business = businessService.getAllBusinessByPageNumber(pageNo, pageSize);
        return ResponseEntity.ok(business);
    }

    @Operation(summary = "update business apis", description = "This is update the business apis ")
    @PatchMapping("{businessId}")
    public ResponseEntity<BusinessResponseDto> updateBusiness(@PathVariable Integer businessId, @RequestBody BusinessRequestDto businessRequestDto)
    {
        BusinessResponseDto updateBusiness = businessService.updateBusiness(businessId, businessRequestDto);
        return ResponseEntity.ok(updateBusiness);
    }

    @Operation(summary = "Business Dashboard API",description = "Returns dashboard data for a specific business")
    @GetMapping("/dashboard/{businessId}")
    public ResponseEntity<BusinessDashboardDto> getDashboard(
            @PathVariable Integer businessId) {

        BusinessDashboardDto dashboard =
                businessService.getDashboardData(businessId);

        return ResponseEntity.ok(dashboard);
    }
    @Operation(summary = "Export Reviews",description = "Download all reviews of a business in CSV format")
    @GetMapping("/export/{businessId}")
    public ResponseEntity<byte[]> exportReviews(
            @PathVariable Integer businessId) {

        byte[] file = businessService.exportReviewsAsCsv(businessId);

        return ResponseEntity.ok()
                .header("Content-Disposition",
                        "attachment; filename=reviews.csv")
                .header("Content-Type", "text/csv")
                .body(file);
    }
    @Operation(summary = "Share Reviews",description = "Generate a public shareable link for reviews")
    @GetMapping("/share/{businessId}")
    public ResponseEntity<String> shareReviews(
            @PathVariable Integer businessId) {

        String link = businessService.generateShareLink(businessId);
        return ResponseEntity.ok(link);
    }

    @Operation(summary = "Review Analytics",description = "Get monthly review analytics")
    @GetMapping("/analytics/{businessId}")
    public ResponseEntity<List<Object[]>> getAnalytics(
            @PathVariable Integer businessId) {

        return ResponseEntity.ok(
                businessService.getMonthlyAnalytics(businessId)
        );
    }

}
