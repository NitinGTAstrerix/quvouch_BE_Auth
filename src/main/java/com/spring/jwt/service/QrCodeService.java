package com.spring.jwt.service;

import com.spring.jwt.dto.AssignQrCodeRequest;
import com.spring.jwt.dto.QrCodeResponse;
import com.spring.jwt.entity.QrCode;

import java.util.List;

public interface QrCodeService {

    byte[] createQrCode(Integer businessId, String location);

    List<QrCodeResponse> getAllQr();

    String EnableQrCode( String qrId);

    String DisableQrCode( String qrId);

    byte[] downloadQrCode(String qrId);

    List<QrCode> getUnassignedQrCodes();

    List<QrCode> getMyAssignedQrCodes();
}