package com.spring.jwt.service.impl;

import com.spring.jwt.dto.BusinessRequestDto;
import com.spring.jwt.dto.BusinessResponseDto;
import com.spring.jwt.dto.QrCodeResponse;
import com.spring.jwt.dto.SalesDashboardDto;
import com.spring.jwt.entity.Business;
import com.spring.jwt.entity.QrCode;
import com.spring.jwt.entity.User;
import com.spring.jwt.repository.BusinessRepository;
import com.spring.jwt.repository.QrCodeRepository;
import com.spring.jwt.service.SalesClientService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesClientServiceImpl implements SalesClientService {

    private final BusinessRepository businessRepository;
    private final QrCodeRepository qrCodeRepository;
    private final ModelMapper modelMapper;

    private User getCurrentUser() {

        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    @Override
    public BusinessResponseDto createClient(BusinessRequestDto dto) {

        User user = getCurrentUser();

        Business business = modelMapper.map(dto, Business.class);
        business.setUser(user);
        business.setStatus(Business.BusinessStatus.PENDING);

        Business saved = businessRepository.save(business);

        return modelMapper.map(saved, BusinessResponseDto.class);
    }

    @Override
    public List<BusinessResponseDto> getMyClients() {

        User user = getCurrentUser();

        return businessRepository.findByUser(user)
                .stream()
                .map(b -> modelMapper.map(b, BusinessResponseDto.class))
                .toList();
    }

    @Override
    public BusinessResponseDto getClientById(Integer id) {

        User user = getCurrentUser();

        Business business = businessRepository
                .findByBusinessIdAndUser(id, user)
                .orElseThrow(() ->
                        new RuntimeException("Client not found"));

        return modelMapper.map(business, BusinessResponseDto.class);
    }

    @Override
    public List<BusinessResponseDto> searchClients(String keyword) {

        User user = getCurrentUser();

        return businessRepository
                .findByUserAndBusinessNameContainingIgnoreCase(user, keyword)
                .stream()
                .map(b -> modelMapper.map(b, BusinessResponseDto.class))
                .toList();
    }

    @Override
    public BusinessResponseDto updateClient(Integer id, BusinessRequestDto dto) {

        User user = getCurrentUser();

        Business business = businessRepository
                .findByBusinessIdAndUser(id, user)
                .orElseThrow(() ->
                        new RuntimeException("Client not found"));

        business.setBusinessName(dto.getBusinessName());
        business.setBusinessType(dto.getBusinessType());
        business.setAddress(dto.getAddress());
        business.setPhoneNumber(dto.getPhoneNumber());

        Business updated = businessRepository.save(business);

        return modelMapper.map(updated, BusinessResponseDto.class);
    }

    @Override
    public void changeStatus(Integer id) {

        User user = getCurrentUser();

        Business business = businessRepository
                .findByBusinessIdAndUser(id, user)
                .orElseThrow(() ->
                        new RuntimeException("Client not found"));

        Business.BusinessStatus status = business.getStatus();

        if (status == Business.BusinessStatus.PENDING) {
            business.setStatus(Business.BusinessStatus.ACTIVE);

        } else if (status == Business.BusinessStatus.ACTIVE) {
            business.setStatus(Business.BusinessStatus.INACTIVE);

        } else {
            business.setStatus(Business.BusinessStatus.ACTIVE);
        }

        businessRepository.save(business);
    }

    @Override
    public SalesDashboardDto getDashboardData() {

        User user = getCurrentUser();

        long total = businessRepository.countByUser(user);

        long active = businessRepository
                .countByUserAndStatus(user, Business.BusinessStatus.ACTIVE);

        long pending = businessRepository
                .countByUserAndStatus(user, Business.BusinessStatus.PENDING);

        long inactive = businessRepository
                .countByUserAndStatus(user, Business.BusinessStatus.INACTIVE);

        long activeQr = qrCodeRepository
                .countByBusiness_UserAndStatus(
                        user,
                        QrCode.QrStatus.ACTIVE
                );

        return SalesDashboardDto.builder()
                .totalClients(total)
                .activeClients(active)
                .pendingClients(pending)
                .inactiveClients(inactive)
                .activeQrCodes(activeQr)
                .build();
    }

    @Override
    public List<BusinessResponseDto> getClientsByStatus(
            Business.BusinessStatus status) {

        User user = getCurrentUser();

        return businessRepository
                .findByUserAndStatus(user, status)
                .stream()
                .map(b -> modelMapper.map(b, BusinessResponseDto.class))
                .toList();
    }

    @Override
    public List<QrCodeResponse> getActiveQrCodes() {

        User user = getCurrentUser();

        return qrCodeRepository
                .findByBusiness_UserAndStatus(
                        user,
                        QrCode.QrStatus.ACTIVE
                )
                .stream()
                .map(qr -> QrCodeResponse.builder()
                        .id(qr.getId())
                        .qrLink(qr.getQrLink())
                        .location(qr.getLocation())
                        .scanCount(qr.getScanCount())
                        .active(true)
                        .businessId(qr.getBusiness().getBusinessId())
                        .build()
                )
                .toList();
    }

    @Override
    public void deleteClient(Integer id) {

        User user = getCurrentUser();

        Business business = businessRepository.findByBusinessIdAndUser(id, user).orElseThrow(() ->new RuntimeException("Client not found"));

        businessRepository.delete(business);
    }
}
