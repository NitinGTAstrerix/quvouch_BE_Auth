package com.spring.jwt.service;

import com.spring.jwt.dto.BusinessRequestDto;
import com.spring.jwt.dto.BusinessResponseDto;
import com.spring.jwt.dto.QrCodeResponseDto;
import com.spring.jwt.dto.SalesDashboardDto;
import com.spring.jwt.entity.BusinessStatus;

import java.util.List;

public interface SalesClientService {

    BusinessResponseDto createClient(BusinessRequestDto dto);

    List<BusinessResponseDto> getMyClients();

    BusinessResponseDto getClientById(Integer id);

    List<BusinessResponseDto> searchClients(String keyword);

    BusinessResponseDto updateClient(Integer id, BusinessRequestDto dto);

    void changeStatus(Integer id);

    SalesDashboardDto getDashboardData();

    List<BusinessResponseDto> getClientsByStatus(BusinessStatus status);

    List<QrCodeResponseDto> getActiveQrCodes();

    void deleteClient(Integer id);
}
