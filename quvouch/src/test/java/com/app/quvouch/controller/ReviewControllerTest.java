package com.app.quvouch.controller;

import com.app.quvouch.entity.Business;
import com.app.quvouch.entity.Review;
import com.app.quvouch.entity.Review.ReviewStatus;
import com.app.quvouch.dtos.ReviewRequestDto;
import com.app.quvouch.dtos.ReviewResponse;
import com.app.quvouch.repository.RoleRepository;
import com.app.quvouch.repository.UserRepository; // <--- Added Import
import com.app.quvouch.security.JwtService;       // <--- Added Import
import com.app.quvouch.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
@AutoConfigureMockMvc(addFilters = false) // Disables the actual execution of security filters
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ReviewRequestDto requestDto;
    private Review mockReview;
    private ReviewResponse mockResponse;
    private UUID qrCodeId;
    private UUID businessId;

    @BeforeEach
    void setUp() {
        qrCodeId = UUID.randomUUID();
        businessId = UUID.randomUUID();

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
        mockReview.setRedirectUrl("http://google.com/review");

        mockResponse = new ReviewResponse();
        mockResponse.setId(mockReview.getId());
        mockResponse.setBusinessId(businessId);
        mockResponse.setBusinessName("Test Cafe");
        mockResponse.setRating(5);
        mockResponse.setStatus(ReviewStatus.PUBLIC);
        mockResponse.setRedirectUrl("http://google.com/review");
        mockResponse.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("POST /rate - Should Return 201 Created for High Rating")
    void testSubmitReview_HighRating() throws Exception {
        when(reviewService.submitReview(any(ReviewRequestDto.class))).thenReturn(mockReview);
        when(modelMapper.map(any(Review.class), eq(ReviewResponse.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/qr/{qrCodeId}/rate", qrCodeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PUBLIC"))
                .andExpect(jsonPath("$.redirectUrl").value("http://google.com/review"));
    }

    @Test
    @DisplayName("POST /rate - Should Return 400 Bad Request for Invalid Rating")
    void testSubmitReview_InvalidRating() throws Exception {
        requestDto.setRating(6);

        mockMvc.perform(post("/api/v1/qr/{qrCodeId}/rate", qrCodeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }
}