package com.spring.jwt.service.impl;

import com.spring.jwt.dto.*;
import com.spring.jwt.entity.Business;
import com.spring.jwt.entity.QrCode;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.BaseException;
import com.spring.jwt.mapper.UserMapper;
import com.spring.jwt.repository.BusinessRepository;
import com.spring.jwt.repository.QrCodeRepository;
import com.spring.jwt.repository.ReviewRepository;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesClientServiceImpl implements SalesClientService {

    private final BusinessRepository businessRepository;
    private final QrCodeRepository qrCodeRepository;
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final UserService userService;
    private final UserMapper userMapper;

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BaseException(
                    String.valueOf(HttpStatus.UNAUTHORIZED.value()),
                    "User is not Authenticated"
            );
        }

        String email = authentication.getName();

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    @Override
    public BusinessResponseDto createClient(BusinessRequestDto dto) {

        User loggedUser = getCurrentUser();

        boolean isAdmin = loggedUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ADMIN"));

        boolean isSaleRep = loggedUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("SALE_REPRESENTATIVE"));

        if (!isAdmin && !isSaleRep) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Only ADMIN or SALE_REPRESENTATIVE can create business"
            );
        }

        if (dto.getClientId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Client ID is required"
            );
        }

        // 🔥 Fetch CLIENT user
        User client = userRepository.findById(dto.getClientId())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Client not found"
                        )
                );

        // 🔒 Ensure the user is actually CLIENT role
        boolean isClientRole = client.getRoles().stream()
                .anyMatch(role -> role.getName().equals("CLIENT"));

        if (!isClientRole) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "User is not a CLIENT"
            );
        }

        // 🔒 Ensure client belongs to this sales rep (unless ADMIN)
        if (isSaleRep) {
            if (client.getSaleRepresentative() == null ||
                    !client.getSaleRepresentative().getId().equals(loggedUser.getId())) {

                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Client is not assigned to you"
                );
            }
        }

        // ✅ Create business properly
        Business business = modelMapper.map(dto, Business.class);
        business.setUser(client);   // 🔥 CRITICAL FIX

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
    @Transactional
    public Business.BusinessStatus changeStatus(Integer businessId) {

        User loggedUser = getCurrentUser();

        boolean isAdmin = loggedUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ADMIN"));

        boolean isSaleRep = loggedUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("SALE_REPRESENTATIVE"));

        Business business;

        // ✅ ADMIN → can toggle any business
        if (isAdmin) {

            business = businessRepository.findById(businessId)
                    .orElseThrow(() ->
                            new ResponseStatusException(
                                    HttpStatus.NOT_FOUND,
                                    "Business not found"
                            )
                    );
        }

        // ✅ SALE_REPRESENTATIVE → only assigned businesses
        else if (isSaleRep) {

            business = businessRepository
                    .findAssignedBusiness(businessId, loggedUser.getId())
                    .orElseThrow(() ->
                            new ResponseStatusException(
                                    HttpStatus.NOT_FOUND,
                                    "Business not found or not assigned to you"
                            )
                    );
        }

        // ✅ CLIENT → only their own business
        else {

            business = businessRepository
                    .findByBusinessIdAndUser(businessId, loggedUser)
                    .orElseThrow(() ->
                            new ResponseStatusException(
                                    HttpStatus.NOT_FOUND,
                                    "Business not found"
                            )
                    );
        }

        // 🔄 Simple ACTIVE ↔ INACTIVE toggle
        Business.BusinessStatus currentStatus = business.getStatus();

        Business.BusinessStatus newStatus =
                (currentStatus == Business.BusinessStatus.ACTIVE)
                        ? Business.BusinessStatus.INACTIVE
                        : Business.BusinessStatus.ACTIVE;

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
    public List<ClientResponseDto> getMyRegisteredClients() {

        User loggedUser = getCurrentUser();

        List<User> clients = userRepository
                .findBySaleRepresentativeAndRoles_Name(loggedUser, "CLIENT");

        return clients.stream()
                .map(userMapper::toClientResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public String deleteBusiness(Integer businessId) {

        reviewRepository.deleteByBusiness_BusinessId(businessId);
        qrCodeRepository.deleteByBusiness_BusinessId(businessId);

        businessRepository.deleteById(businessId);

        try {

            Authentication authentication = SecurityContextHolder
                    .getContext()
                    .getAuthentication();

            System.out.println("Auth User: " + authentication);

            String email = authentication.getName();
            System.out.println("Email: " + email);

            User loggedUser = userRepository.findByEmail(email);
            System.out.println("Logged User: " + loggedUser);

            Business business = businessRepository.findById(businessId)
                    .orElse(null);

            System.out.println("Business: " + business);

            User client = business.getUser();
            System.out.println("Client: " + client);

            System.out.println("Sale Rep: " + client.getSaleRepresentative());

            businessRepository.delete(business);

            return "Business Deleted Successfully";

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}