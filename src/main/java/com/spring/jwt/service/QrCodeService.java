package com.spring.jwt.service;

import com.spring.jwt.dto.QrCodeResponse;

import java.util.List;

public interface QrCodeService {
    byte[] createQrCode(Integer businessId);

    List<QrCodeResponse> getAllQr();

    String EnableQrCode( String qrId);

    String DisableQrCode( String qrId);
}
