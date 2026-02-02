package com.spring.jwt.services;

import com.spring.jwt.dto.QrCodeResponse;
import com.spring.jwt.entity.Business;
import com.spring.jwt.entity.QrCode;
import com.spring.jwt.exception.BusinessNotFound;
import com.spring.jwt.mapper.QrCodeMapper;
import com.spring.jwt.repository.BusinessRepository;
import com.spring.jwt.repository.QrCodeRepository;
import com.spring.jwt.service.impl.QrCodeServiceImpl;
import com.spring.jwt.utils.QrCodeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QrCodeServiceImplTest {

    @InjectMocks
    private QrCodeServiceImpl qrCodeService;

    @Mock
    private QrCodeRepository qrCodeRepository;

    @Mock
    private BusinessRepository businessRepository;

    @Mock
    private QrCodeGenerator qrCodeGenerator;

    @Mock
    private QrCodeMapper qrCodeMapper;

    private Business business;

    @BeforeEach
    void setup() {
        business = new Business();
        business.setBusinessId(1);
    }

    // ---------- CREATE QR CODE ----------

    @Test
    void shouldCreateQrCodeSuccessfully() {

        when(businessRepository.findById(1))
                .thenReturn(Optional.of(business));

        when(qrCodeGenerator.generateQrCode(anyString(), anyInt(), anyInt()))
                .thenReturn(new byte[]{1, 2, 3});

        byte[] result = qrCodeService.createQrCode(1);

        assertNotNull(result);
        verify(qrCodeRepository, times(1)).save(any(QrCode.class));
    }

    @Test
    void shouldThrowExceptionWhenBusinessNotFound() {

        when(businessRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(BusinessNotFound.class,
                () -> qrCodeService.createQrCode(1));
    }

    // ---------- GET ALL QR ----------

    @Test
    void shouldReturnAllQrCodes() {

        QrCode qrCode = new QrCode();
        QrCodeResponse response = new QrCodeResponse(
                "qr123",
                "http//localhost:8080/qr/scan/324kfasdfklfj3234324",
                "ENTRANCE",
                0,
                true,
                1);

        when(qrCodeRepository.findAll())
                .thenReturn(List.of(qrCode));

        when(qrCodeMapper.toQrCode(qrCode))
                .thenReturn(response);

        List<QrCodeResponse> result = qrCodeService.getAllQr();

        assertEquals(1, result.size());
        verify(qrCodeRepository).findAll();
    }

    // ---------- ENABLE QR ----------

    @Test
    void shouldEnableQrCode() {

        QrCode qrCode = new QrCode();
        qrCode.setActive(false);

        when(qrCodeRepository.findById("qr123"))
                .thenReturn(Optional.of(qrCode));

        String result = qrCodeService.EnableQrCode("qr123");

        assertEquals("Qr Code Enabled", result);
        assertTrue(qrCode.isActive());
        verify(qrCodeRepository).save(qrCode);
    }

    // ---------- DISABLE QR ----------

    @Test
    void shouldDisableQrCode() {

        QrCode qrCode = new QrCode();
        qrCode.setActive(true);

        when(qrCodeRepository.findById("qr123"))
                .thenReturn(Optional.of(qrCode));

        String result = qrCodeService.DisableQrCode("qr123");

        assertEquals("Qr Code Disable", result);
        assertFalse(qrCode.isActive());
        verify(qrCodeRepository).save(qrCode);
    }
}
