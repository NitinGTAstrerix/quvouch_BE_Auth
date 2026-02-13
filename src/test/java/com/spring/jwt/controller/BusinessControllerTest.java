package com.spring.jwt.controller;

import com.spring.jwt.dto.BusinessResponseDto;
import com.spring.jwt.mapper.BusinessMapper;
import com.spring.jwt.service.BusinessService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BusinessControllerTest.class)
public class BusinessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BusinessService businessService;

    @MockBean
    private BusinessMapper businessMapper;

    @Test
    public void createBusiness_success() throws Exception {
        when(businessService.createBusiness(any()))
                .thenReturn(BusinessResponseDto.builder()
                        .businessName("Test")
                        .businessType("IT")
                        .address("pune")
<<<<<<< HEAD
                        .phoneNumber(9876543210L)
=======
                        .phoneNumber("9876543210L")
                        .businessEmail("business@gmail.com")
>>>>>>> fec191664a493331f12a1e1fed807664e99cd9c4
                        .build());

        mockMvc.perform(post("/api/v1/business")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "businessName": "Test",
                          "businessType": "IT",
                          "address" : "pune",
                          "phoneNumber" : "9876543210"
<<<<<<< HEAD
=======
                          "businessEmail" : "business@gmail.com"
>>>>>>> fec191664a493331f12a1e1fed807664e99cd9c4
                        }
                        """))
                .andExpect(status().isCreated());
    }

    @Test
    public void getBusinessById_success() throws Exception {
        when(businessService.getBusinessById(1))
                .thenReturn(BusinessResponseDto.builder().businessId(1).build());

        mockMvc.perform(get("/api/v1/business/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllBusinesses_success() throws Exception {
        when(businessService.getAllBusiness())
                .thenReturn(List.of(new BusinessResponseDto()));

        mockMvc.perform(get("/api/v1/business"))
                .andExpect(status().isOk());
    }

    @Test
    public void getBusinessByPage_success() throws Exception {
        Page<BusinessResponseDto> page =
                new PageImpl<>(List.of(new BusinessResponseDto()));

        when(businessService.getAllBusinessByPageNumber(0, 10))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/business/page")
                        .param("pageNo", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk());
    }

    @Test
    public void updateBusiness_success() throws Exception {
        when(businessService.updateBusiness(eq(1), any()))
                .thenReturn(BusinessResponseDto.builder().build());

        mockMvc.perform(patch("/api/v1/business/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "businessName": "Updated"
                        }
                        """))
                .andExpect(status().isOk());
    }
}
