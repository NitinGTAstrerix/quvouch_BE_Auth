package com.spring.jwt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.jwt.dto.ReviewRequestDto;
import com.spring.jwt.dto.ReviewResponse;
import com.spring.jwt.entity.Business;
import com.spring.jwt.entity.Review;
import com.spring.jwt.entity.Review.ReviewStatus;
import com.spring.jwt.jwt.JwtService;
import com.spring.jwt.mapper.ReviewMapper;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.service.ReviewService;
import com.spring.jwt.utils.DecryptionResponseProcessor;
import com.spring.jwt.utils.EncryptionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private ReviewMapper reviewMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private EncryptionUtil encryptionUtil;

    @MockBean
    private DecryptionResponseProcessor decryptionResponseProcessor;

    private ReviewRequestDto requestDto;
    private Review mockReview;
    private ReviewResponse mockResponse;
    private UUID qrCodeId;
    private Integer businessId;

    @BeforeEach
    void setUp() {
        qrCodeId = UUID.randomUUID();
        businessId = 10001;

        requestDto = new ReviewRequestDto();
        requestDto.setBusinessId(businessId);
        requestDto.setQrCodeId(qrCodeId);
        requestDto.setRating(5);
        requestDto.setCustomerName("Test User");

        Business business = new Business();
        business.setBusinessId(businessId);
        business.setBusinessName("Test Cafe");

        mockReview = new Review();
        mockReview.setId(UUID.randomUUID());
        mockReview.setBusiness(business);
        mockReview.setRating(5);
        mockReview.setStatus(ReviewStatus.PUBLIC);

        mockResponse = new ReviewResponse();
        mockResponse.setId(mockReview.getId());
        mockResponse.setBusinessId(businessId);
        mockResponse.setBusinessName("Test Cafe");
        mockResponse.setRating(5);
        mockResponse.setStatus(ReviewStatus.PUBLIC);
        mockResponse.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("POST /rate - Should Return 201 Created")
    void testSubmitReview_Success() throws Exception {
        when(reviewService.submitReview(any(ReviewRequestDto.class))).thenReturn(mockReview);

        when(reviewMapper.toResponse(any(Review.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/qr/{qrCodeId}/rate", qrCodeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.status").value("PUBLIC"));
    }

    @Test
    @DisplayName("POST /rate - Should Return 400 for Invalid Data")
    void testSubmitReview_Invalid() throws Exception {
        requestDto.setRating(6);

        mockMvc.perform(post("/api/v1/qr/{qrCodeId}/rate", qrCodeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }
}