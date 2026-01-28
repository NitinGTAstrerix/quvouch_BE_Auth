package com.spring.jwt.controller;

import com.spring.jwt.dto.BusinessRequestDto;
import com.spring.jwt.dto.BusinessResponseDto;
import com.spring.jwt.mapper.BusinessMapper;
import com.spring.jwt.service.BusinessService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/business")
public class BusinessController {

    private final BusinessService businessService;
    private final BusinessMapper businessMapper;

    @PostMapping
    public ResponseEntity<BusinessResponseDto> creteBusiness(@RequestBody BusinessRequestDto businessRequestDto)
    {
        BusinessResponseDto business = businessService.createBusiness(businessRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(business);
    }

    @GetMapping("{businessId}")
    public ResponseEntity<BusinessResponseDto> getBusinessById(@PathVariable Integer businessId)
    {
        BusinessResponseDto business = businessService.getBusinessById(businessId);
        return ResponseEntity.ok(business);
    }

    @GetMapping
    public ResponseEntity<List<BusinessResponseDto>> getBusinesses()
    {
        List<BusinessResponseDto> allBusiness = businessService.getAllBusiness();
        return ResponseEntity.ok(allBusiness);
    }

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
