package com.spring.jwt.controller;

import com.spring.jwt.dto.BusinessRequestDto;
import com.spring.jwt.dto.BusinessResponseDto;
import com.spring.jwt.mapper.BusinessMapper;
import com.spring.jwt.service.BusinessService;
<<<<<<< HEAD
=======
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
>>>>>>> fec191664a493331f12a1e1fed807664e99cd9c4
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
<<<<<<< HEAD
=======
@Tag(name = "Business controller its for client")
>>>>>>> fec191664a493331f12a1e1fed807664e99cd9c4
public class BusinessController {

    private final BusinessService businessService;
    private final BusinessMapper businessMapper;

<<<<<<< HEAD
=======
    @Operation(summary = "create business apis", description = "This is create the business apis ")
    @SecurityRequirement(name = "bearerAuth")
>>>>>>> fec191664a493331f12a1e1fed807664e99cd9c4
    @PreAuthorize("hasAnyAuthority('ADMIN','SALE_REPRESENTATIVE')")
    @PostMapping
    public ResponseEntity<BusinessResponseDto> creteBusiness(@Valid @RequestBody BusinessRequestDto businessRequestDto)
    {
        BusinessResponseDto business = businessService.createBusiness(businessRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(business);
    }

<<<<<<< HEAD
=======
    @Operation(summary = "get api by id", description = "This is create the business apis ")
    @SecurityRequirement(name = "bearerAuth")
>>>>>>> fec191664a493331f12a1e1fed807664e99cd9c4
    @PreAuthorize("hasAnyAuthority('ADMIN','SALE_REPRESENTATIVE')")
    @GetMapping("{businessId}")
    public ResponseEntity<BusinessResponseDto> getBusinessById(@PathVariable Integer businessId)
    {
        BusinessResponseDto business = businessService.getBusinessById(businessId);
        return ResponseEntity.ok(business);
    }

<<<<<<< HEAD
=======
    @Operation(summary = "own business apis", description = "get own the business ")
    @SecurityRequirement(name = "bearerAuth")
>>>>>>> fec191664a493331f12a1e1fed807664e99cd9c4
    @GetMapping("/own")
    public ResponseEntity<BusinessResponseDto> getBusinessOwn()
    {
        BusinessResponseDto businessByOwn = businessService.getBusinessByOwn();
        return ResponseEntity.ok(businessByOwn);
    }

<<<<<<< HEAD
=======
    @Operation(summary = "all  businesses ", description = "admin get all business")
    @SecurityRequirement(name = "bearerAuth")
>>>>>>> fec191664a493331f12a1e1fed807664e99cd9c4
    @PreAuthorize("hasAnyAuthority('ADMIN','SALE_REPRESENTATIVE')")
    @GetMapping
    public ResponseEntity<List<BusinessResponseDto>> getBusinesses()
    {
        List<BusinessResponseDto> allBusiness = businessService.getAllBusiness();
        return ResponseEntity.ok(allBusiness);
    }

<<<<<<< HEAD
=======
    @Operation(summary = "get business by pagination", description = "get business by pages ")
    @SecurityRequirement(name = "bearerAuth")
>>>>>>> fec191664a493331f12a1e1fed807664e99cd9c4
    @PreAuthorize("hasAnyAuthority('ADMIN','SALE_REPRESENTATIVE')")
    @GetMapping("/page")
    public ResponseEntity<Page<BusinessResponseDto>> getBusinessByPageNumber(@RequestParam int pageNo, @RequestParam int pageSize)
    {
        Page<BusinessResponseDto> business = businessService.getAllBusinessByPageNumber(pageNo, pageSize);
        return ResponseEntity.ok(business);
    }

<<<<<<< HEAD
=======
    @Operation(summary = "update business apis", description = "This is update the business apis ")
>>>>>>> fec191664a493331f12a1e1fed807664e99cd9c4
    @PatchMapping("{businessId}")
    public ResponseEntity<BusinessResponseDto> updateBusiness(@PathVariable Integer businessId, @RequestBody BusinessRequestDto businessRequestDto)
    {
        BusinessResponseDto updateBusiness = businessService.updateBusiness(businessId, businessRequestDto);
        return ResponseEntity.ok(updateBusiness);
    }
}
