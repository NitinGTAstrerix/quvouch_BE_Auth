package com.spring.jwt.controller;

import com.spring.jwt.dto.BusinessRequestDto;
import com.spring.jwt.dto.BusinessResponseDto;
import com.spring.jwt.mapper.BusinessMapper;
import com.spring.jwt.service.BusinessService;
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
public class BusinessController {

    private final BusinessService businessService;
    private final BusinessMapper businessMapper;

    @PreAuthorize("hasAnyAuthority('ADMIN','SALE_REPRESENTATIVE')")
    @PostMapping
    public ResponseEntity<BusinessResponseDto> creteBusiness(@Valid @RequestBody BusinessRequestDto businessRequestDto)
    {
        BusinessResponseDto business = businessService.createBusiness(businessRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(business);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SALE_REPRESENTATIVE')")
    @GetMapping("{businessId}")
    public ResponseEntity<BusinessResponseDto> getBusinessById(@PathVariable Integer businessId)
    {
        BusinessResponseDto business = businessService.getBusinessById(businessId);
        return ResponseEntity.ok(business);
    }

    @GetMapping("/own")
    public ResponseEntity<BusinessResponseDto> getBusinessOwn()
    {
        BusinessResponseDto businessByOwn = businessService.getBusinessByOwn();
        return ResponseEntity.ok(businessByOwn);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SALE_REPRESENTATIVE')")
    @GetMapping
    public ResponseEntity<List<BusinessResponseDto>> getBusinesses()
    {
        List<BusinessResponseDto> allBusiness = businessService.getAllBusiness();
        return ResponseEntity.ok(allBusiness);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','SALE_REPRESENTATIVE')")
    @GetMapping("/page")
    public ResponseEntity<Page<BusinessResponseDto>> getBusinessByPageNumber(@RequestParam int pageNo, @RequestParam int pageSize)
    {
        Page<BusinessResponseDto> business = businessService.getAllBusinessByPageNumber(pageNo, pageSize);
        return ResponseEntity.ok(business);
    }

    @PatchMapping("{businessId}")
    public ResponseEntity<BusinessResponseDto> updateBusiness(@PathVariable Integer businessId, @RequestBody BusinessRequestDto businessRequestDto)
    {
        BusinessResponseDto updateBusiness = businessService.updateBusiness(businessId, businessRequestDto);
        return ResponseEntity.ok(updateBusiness);
    }
}
