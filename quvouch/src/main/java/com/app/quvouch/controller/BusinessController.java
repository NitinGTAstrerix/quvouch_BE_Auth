package com.app.quvouch.controller;

import com.app.quvouch.dtos.BusinessRequest;
import com.app.quvouch.dtos.BusinessResponse;
import com.app.quvouch.service.BusinessService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/business")
public class BusinessController {

    private final BusinessService businessService;

    @PostMapping
    public ResponseEntity<BusinessRequest> createBusinessHandler(@RequestBody BusinessRequest businessRequest)
    {
        BusinessRequest business = businessService.createBusiness(businessRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(business);
    }

    @GetMapping("{businessId}")
    public ResponseEntity<BusinessResponse> getBusinessById(@PathVariable UUID businessId)
    {
        BusinessResponse business = businessService.getBusinessById(businessId);
        return ResponseEntity.ok(business);
    }

    @GetMapping
    public ResponseEntity<Page<BusinessResponse>> getAllBusiness(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "businessName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    )
    {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<BusinessResponse> allBusiness = businessService.getAllBusiness(pageable);
        return ResponseEntity.ok(allBusiness);
    }

    @PatchMapping("{businessId}")
    public ResponseEntity<BusinessRequest> patchBusiness(@PathVariable UUID businessId, @RequestBody BusinessRequest request)
    {
        BusinessRequest updateBusiness = businessService.updateBusiness(businessId, request);
        return ResponseEntity.ok(updateBusiness);
    }

    @DeleteMapping("{businessId}")
    public ResponseEntity<String> deleteBusiness(@PathVariable UUID businessId)
    {
        String s = businessService.deleteBusiness(businessId);
        return ResponseEntity.ok(s);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<BusinessResponse>> searchBusinessWithName(
            @RequestParam String key,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "businessName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection)
    {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<BusinessResponse> businessByName = businessService.getBusinessByName(key, pageable);
        return ResponseEntity.ok(businessByName);
    }
}
