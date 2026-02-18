package com.spring.jwt.service;

import com.spring.jwt.dto.*;
import com.spring.jwt.entity.Business;
import com.spring.jwt.entity.QrCode;

import java.util.List;

public interface SalesClientService {

    BusinessResponseDto createClient(BusinessRequestDto dto);

    List<BusinessResponseDto> getMyClients();

    BusinessResponseDto getClientById(Integer id);

    List<BusinessResponseDto> searchClients(String keyword);

    BusinessResponseDto updateClient(Integer id, BusinessRequestDto dto);

    Business.BusinessStatus changeStatus(Integer id);

    SalesDashboardDto getDashboardData();

    List<BusinessResponseDto> getClientsByStatus(Business.BusinessStatus status);

    List<QrCodeResponse> getActiveQrCodes();

    void deleteClient(Integer id);

    void assignQrToBusiness(AssignQrCodeRequest request);

    List<QrCode> getUnassignedQrCodes();

    List<QrCode> getMyAssignedQrCodes();
}
