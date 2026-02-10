package com.spring.jwt.services;

import com.spring.jwt.dto.BusinessRequestDto;
import com.spring.jwt.dto.BusinessResponseDto;
import com.spring.jwt.entity.Business;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.BaseException;
import com.spring.jwt.exception.BusinessNotFound;
import com.spring.jwt.mapper.BusinessMapper;
import com.spring.jwt.repository.BusinessRepository;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.service.impl.BusinessServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class BusinessServiceImplTest {

    @Mock
    private BusinessRepository businessRepository;

    @Mock
    private BusinessMapper businessMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BusinessServiceImpl businessService;

    // ---------- CREATE BUSINESS ----------

    @Test
    void createBusiness_success() {

        BusinessRequestDto request = BusinessRequestDto.builder()
                .businessName("abcd services")
                .businessType("IT")
                .address("abc pune")
                .phoneNumber("434343434343L")
                .build();
        log.info("Starting to create business");
        Business business = new Business();
        User user = new User();
        user.setId(1);
        log.info("user is created");
        // Mock Security Context
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("test@mail.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Mockito stubs
        when(businessMapper.toBusinessRequest(request)).thenReturn(business);
        when(userRepository.findByEmail("test@mail.com")).thenReturn(user);
        when(businessRepository.save(any(Business.class))).thenReturn(business);
        when(businessMapper.toBusiness(any()))
                .thenReturn(BusinessResponseDto.builder().businessName("My Business").build());

        BusinessResponseDto response = businessService.createBusiness(request);
        log.info("business is created {}",response.getBusinessName());
        assertNotNull(response);
        verify(businessRepository, times(1)).save(any(Business.class));
    }

    @Test
    void createBusiness_whenBusinessNameEmpty_throwException() {

        BusinessRequestDto request = new BusinessRequestDto();

        BaseException exception = assertThrows(
                BaseException.class,
                () -> businessService.createBusiness(request)
        );

        assertEquals("Please Enter Business Name", exception.getMessage());
    }

    // ---------- GET BUSINESS ----------

    @Test
    void getBusinessById_success() {

        Business business = new Business();
        business.setBusinessId(2);

        when(businessRepository.findById(1)).thenReturn(Optional.of(business));
        when(businessMapper.toBusiness(business))
                .thenReturn(BusinessResponseDto.builder().build());
        log.info("get Business by Id : {}",business.getBusinessName());
        BusinessResponseDto response = businessService.getBusinessById(1);
        log.info("get Business by Id : {}",response.getBusinessName());
        assertNotNull(response);
    }

    @Test
    void getBusinessById_notFound() {

        when(businessRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> businessService.getBusinessById(1)
        );
    }

    @Test
    void getAllBusiness_success() {

        when(businessRepository.findAll())
                .thenReturn(List.of(new Business(), new Business()));

        when(businessMapper.toBusiness(any()))
                .thenReturn(BusinessResponseDto.builder().build());

        List<BusinessResponseDto> result = businessService.getAllBusiness();

        assertEquals(2, result.size());
    }

    // ---------- UPDATE BUSINESS ----------

    @Test
    void updateBusiness_success() {

        Business business = new Business();
        business.setBusinessName("Old Name");

        BusinessRequestDto request = BusinessRequestDto.builder()
                .businessName("New Name")
                .build();

        when(businessRepository.findById(1)).thenReturn(Optional.of(business));
        when(businessRepository.save(any(Business.class))).thenReturn(business);
        when(businessMapper.toBusiness(any()))
                .thenReturn(BusinessResponseDto.builder().build());
        log.info("update business request {}",request.getBusinessName());
        BusinessResponseDto response = businessService.updateBusiness(1, request);
        log.info("change the name of the business {}",response.getBusinessName());
        assertNotNull(response);
        assertEquals("New Name", business.getBusinessName());
    }

    @Test
    void updateBusiness_notFound() {

        when(businessRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(
                BusinessNotFound.class,
                () -> businessService.updateBusiness(1, new BusinessRequestDto())
        );
    }
}
