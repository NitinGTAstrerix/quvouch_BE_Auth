package com.spring.jwt.controller;

import com.spring.jwt.dto.QrCodeResponse;
import com.spring.jwt.service.QrCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@WebMvcTest({QrCodeController.class, QrCodeController.class})
class QrCodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QrCodeService qrCodeService;

    // ---------- GENERATE QR ----------

    @Test
    @WithMockUser(authorities = "ADMIN")
    void shouldGenerateQrCodeImage() throws Exception {

        when(qrCodeService.createQrCode(1))
                .thenReturn(new byte[]{1, 2, 3});

        mockMvc.perform(get("/api/v1/qrcode/generate/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG));
    }

    // ---------- GET ALL QR ----------

    @Test
    @WithMockUser(authorities = "ADMIN")
    void shouldGetAllQrCodesForAdmin() throws Exception {

        when(qrCodeService.getAllQr())
                .thenReturn(List.of(new QrCodeResponse(
                        "qr123",
                        "https://test/qr/qr123",
                        "ENTRANCE",
                        0,
                        true,
                        1)));

        mockMvc.perform(get("/api/v1/qrcode"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFailGetAllQrWithoutAuth() throws Exception {

        mockMvc.perform(get("/api/v1/qrcode"))
                .andExpect(status().isUnauthorized()); // ✅
    }

    // ---------- ENABLE QR ----------

    @Test
    @WithMockUser(authorities = "SALE_REPRESENTATIVE")
    void shouldEnableQrCode() throws Exception {

        when(qrCodeService.EnableQrCode("qr123"))
                .thenReturn("Qr Code Enabled");

        mockMvc.perform(put("/api/v1/qrcode/qr123/enable"))
                .andExpect(status().isOk());
    }

    // ---------- DISABLE QR ----------

    @Test
    @WithMockUser(authorities = "OWNER")
    void shouldFailDisableQrForOwner() throws Exception {

        mockMvc.perform(put("/api/v1/qrcode/qr123/disable"))
                .andExpect(status().isForbidden()); // ✅ works now
    }
}
