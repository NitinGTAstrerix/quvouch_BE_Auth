package com.spring.jwt.service.impl;

import com.spring.jwt.dto.*;
import com.spring.jwt.entity.Business;
import com.spring.jwt.entity.QrCode;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.BaseException;
import com.spring.jwt.repository.BusinessRepository;
import com.spring.jwt.repository.QrCodeRepository;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.service.SalesClientService;
import com.spring.jwt.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesClientServiceImpl implements SalesClientService {

    private final BusinessRepository businessRepository;
    private final QrCodeRepository qrCodeRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final UserService userService;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
        {
            throw new BaseException(String.valueOf(HttpStatus.UNAUTHORIZED.value()),"User is not Authenticated");
        }
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        if (user == null)
        {
            throw new UsernameNotFoundException("User is not found");
        }

        return user;
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
    public Business.BusinessStatus changeStatus(Integer id) {

        User user = getCurrentUser();

        Business business = businessRepository
                .findByBusinessIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Client not found"
                        )
                );

        if (business.getStatus() == null) {
            business.setStatus(Business.BusinessStatus.PENDING);
        }

        Business.BusinessStatus newStatus =
                business.getStatus().next();

        business.setStatus(newStatus);

        businessRepository.save(business);
        return newStatus;
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


    @Override
    @Transactional
    public void assignQrToBusiness(AssignQrCodeRequest request) {

        QrCode qrCode = qrCodeRepository.findById(request.getQrCodeId())
                .orElseThrow(() -> new RuntimeException("QR Code not found"));

        if (qrCode.getStatus() != QrCode.QrStatus.UNASSIGNED) {
            throw new RuntimeException("QR Code already assigned");
        }

        Business business = businessRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Business not found"));

        // ✅ Correct way using your DTO structure
        UserProfileDTO profile = userService.getCurrentUserProfile();

        String userIdStr = profile.getUser().getUserId();

        Long salesUserId = Long.parseLong(userIdStr);

        User salesUser = userRepository.findById(salesUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        qrCode.setBusiness(business);
        qrCode.setAssignedBy(salesUser);
        qrCode.setAssignedAt(LocalDateTime.now());
        qrCode.setLocation(request.getLocationLabel());
        qrCode.setStatus(QrCode.QrStatus.ASSIGNED);

        qrCodeRepository.save(qrCode);
    }

    @Override
    public List<QrCode> getUnassignedQrCodes() {

        return qrCodeRepository.findByStatus(QrCode.QrStatus.UNASSIGNED);
    }

    @Override
    public List<QrCode> getMyAssignedQrCodes() {

        UserProfileDTO profile = userService.getCurrentUserProfile();

        String userIdStr = profile.getUser().getUserId();

        Long salesUserId = Long.parseLong(userIdStr);

        User salesUser = userRepository.findById(salesUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return qrCodeRepository.findByAssignedBy(salesUser);
    }
}
