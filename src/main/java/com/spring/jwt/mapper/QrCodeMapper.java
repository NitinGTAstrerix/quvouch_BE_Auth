package com.spring.jwt.mapper;

import org.springframework.stereotype.Component;

import com.spring.jwt.dto.QrCodeResponse;
import com.spring.jwt.entity.QrCode;

@Component
public class QrCodeMapper {
    public QrCodeResponse toQrCode(QrCode qrCode)
    {
        return QrCodeResponse.builder().scanCount(qrCode.getScanCount())
                .location(qrCode.getLocation())
                .businessId(qrCode.getBusiness().getBusinessId())
                .active(qrCode.isActive())
                .qrLink(qrCode.getQrLink())
                .id(qrCode.getId())
                .build();
    }
}
