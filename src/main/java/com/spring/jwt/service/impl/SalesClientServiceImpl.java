package com.spring.jwt.service.impl;

import com.spring.jwt.dto.*;
import com.spring.jwt.entity.Business;
import com.spring.jwt.entity.QrCode;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.BaseException;
import com.spring.jwt.mapper.UserMapper;
import com.spring.jwt.repository.*;
import com.spring.jwt.service.SalesClientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalesClientServiceImpl implements SalesClientService {

    private final BusinessRepository businessRepository;
    private final QrCodeRepository qrCodeRepository;
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
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
    @Transactional
    public BusinessResponseDto createClient(BusinessRequestDto dto) {

        // 1. Get logged-in user
        User loggedUser = getCurrentUser();

        // 2. Check role
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

        // 3. Find CLIENT using clientId
        User client = userRepository.findById(dto.getClientId())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Client not found with ID: " + dto.getClientId()
                        )
                );

        // 4. Verify selected user has CLIENT role
        boolean isClient = client.getRoles().stream()
                .anyMatch(role -> role.getName().equals("CLIENT"));

        if (!isClient) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Selected user is not a CLIENT"
            );
        }

        // 5. Verify client belongs to logged-in Sales Representative
        if (isSaleRep) {

            if (client.getSaleRepresentative() == null ||
                    !client.getSaleRepresentative()
                            .getId()
                            .equals(loggedUser.getId())) {

                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Client is not assigned to this Sales Representative"
                );
            }
        }

        // 6. Create Business
        Business business = Business.builder()
                .businessName(dto.getBusinessName())
                .businessEmail(dto.getBusinessEmail())
                .businessType(dto.getBusinessType())
                .address(dto.getAddress())
                .phoneNumber(dto.getPhoneNumber())
                .status(Business.BusinessStatus.ACTIVE)

                // Sales Representative
                .user(loggedUser)

                // Client
                .client(client)

                .build();

        // 7. Save Business
        Business saved = businessRepository.save(business);

        // 8. Return response
        return modelMapper.map(saved, BusinessResponseDto.class);
    }

    @Override
    public List<BusinessResponseDto> getMyClients() {

        User user = getCurrentUser();

        return businessRepository.findByUser(user)
                .stream()
                .map(b -> {
                    BusinessResponseDto dto = modelMapper.map(b, BusinessResponseDto.class);
                    dto.setQrCodeCount(b.getQrCode() != null ? b.getQrCode().size() : 0);
                    return dto;
                })
                .toList();
    }

    @Override
    public BusinessResponseDto getClientById(Integer id) {

        User user = getCurrentUser();

        Business business = businessRepository
                .findByBusinessIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        BusinessResponseDto dto = modelMapper.map(business, BusinessResponseDto.class);
        dto.setQrCodeCount(business.getQrCode() != null ? business.getQrCode().size() : 0);
        return dto;
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
                    .findByBusinessIdAndUser(businessId, loggedUser)
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

        User loggedUser = getCurrentUser();

        List<Business> businesses = businessRepository.findByUser(loggedUser);

        long total = businesses.size();
        long active = businesses.stream()
                .filter(b -> b.getStatus() == Business.BusinessStatus.ACTIVE)
                .count();
        long inactive = total - active;

        long activeQr = qrCodeRepository
                .findByBusinessInAndStatus(
                        businesses,
                        QrCode.QrStatus.ACTIVE
                )
                .size();

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

        User loggedUser = getCurrentUser();

        System.out.println("Logged User : " + loggedUser.getEmail());

        List<Business> businesses = businessRepository.findByUser(loggedUser);

        System.out.println("Businesses Count : " + businesses.size());

        businesses.forEach(b ->
                System.out.println("Business ID : " + b.getBusinessId())
        );

        List<QrCode> qrCodes = qrCodeRepository.findByBusinessInAndStatus(
                businesses,
                QrCode.QrStatus.ACTIVE
        );

        System.out.println("Active QR Count : " + qrCodes.size());

        qrCodes.forEach(q ->
                System.out.println(
                        q.getId() + " Status=" + q.getStatus() +
                                " Active=" + q.isActive() +
                                " Business=" + q.getBusiness().getBusinessId()
                )
        );

        return qrCodes.stream()
                .map(qr -> QrCodeResponse.builder()
                        .id(qr.getId())
                        .qrLink(qr.getQrLink())
                        .location(qr.getLocation())
                        .scanCount(qr.getScanCount())
                        .active(qr.isActive())
                        .businessId(qr.getBusiness().getBusinessId())
                        .build())
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
    public byte[] generateReport() {

        User loggedUser = getCurrentUser();

        List<Business> businesses =
                businessRepository.findByUser(loggedUser);

        long totalClients = businesses.size();

        long activeClients = businesses.stream()
                .filter(b -> b.getStatus() == Business.BusinessStatus.ACTIVE)
                .count();

        long inactiveClients = businesses.stream()
                .filter(b -> b.getStatus() == Business.BusinessStatus.INACTIVE)
                .count();

        long totalQrCodes =
                qrCodeRepository.findByBusinessIn(businesses).size();

        long activeQrCodes =
                qrCodeRepository.findByBusinessInAndStatus(
                        businesses,
                        QrCode.QrStatus.ACTIVE
                ).size();

        long totalReviews = businesses.stream()
                .mapToLong(b ->
                        reviewRepository
                                .findByBusiness_BusinessId(b.getBusinessId())
                                .size()
                )
                .sum();

        try (ByteArrayOutputStream outputStream =
                     new ByteArrayOutputStream()) {

            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Title
            document.add(
                    new Paragraph("Sales Representative Report")
                            .setBold()
                            .setFontSize(20)
            );

            document.add(
                    new Paragraph(
                            "Sales Representative: " +
                                    loggedUser.getEmail()
                    )
            );

            document.add(new Paragraph(" "));

            // Dashboard Summary
            document.add(
                    new Paragraph("Dashboard Summary")
                            .setBold()
                            .setFontSize(14)
            );

            Table summaryTable = new Table(2);

            summaryTable.addCell("Total Clients");
            summaryTable.addCell(String.valueOf(totalClients));

            summaryTable.addCell("Active Clients");
            summaryTable.addCell(String.valueOf(activeClients));

            summaryTable.addCell("Inactive Clients");
            summaryTable.addCell(String.valueOf(inactiveClients));

            summaryTable.addCell("Total QR Codes");
            summaryTable.addCell(String.valueOf(totalQrCodes));

            summaryTable.addCell("Active QR Codes");
            summaryTable.addCell(String.valueOf(activeQrCodes));

            summaryTable.addCell("Total Reviews");
            summaryTable.addCell(String.valueOf(totalReviews));

            document.add(summaryTable);

            document.add(new Paragraph(" "));

            // Client Details
            document.add(
                    new Paragraph("Client Details")
                            .setBold()
                            .setFontSize(14)
            );

            Table clientTable = new Table(5);

            clientTable.addHeaderCell("Business ID");
            clientTable.addHeaderCell("Business Name");
            clientTable.addHeaderCell("Status");
            clientTable.addHeaderCell("QR Codes");
            clientTable.addHeaderCell("Reviews");

            for (Business business : businesses) {

                long qrCount =
                        qrCodeRepository
                                .findByBusinessIn(List.of(business))
                                .size();

                long reviewCount =
                        reviewRepository
                                .findByBusiness_BusinessId(
                                        business.getBusinessId()
                                )
                                .size();

                clientTable.addCell(
                        String.valueOf(business.getBusinessId())
                );

                clientTable.addCell(
                        business.getBusinessName()
                );

                clientTable.addCell(
                        business.getStatus().name()
                );

                clientTable.addCell(
                        String.valueOf(qrCount)
                );

                clientTable.addCell(
                        String.valueOf(reviewCount)
                );
            }

            document.add(clientTable);

            document.close();

            return outputStream.toByteArray();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to generate sales report",
                    e
            );
        }
    }

    @Override
    @Transactional
    public String deleteBusiness(Integer businessId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated.");
        }

        String email = authentication.getName();

        User salesRepresentative = userRepository.findByEmail(email);

        if (salesRepresentative == null) {
            throw new RuntimeException("Sales Representative not found.");
        }

        Business business = businessRepository.findById(businessId)
                .orElseThrow(() ->
                        new RuntimeException("Business not found."));

        //User client = business.getUser();
        User client = business.getClient();

        if (client == null ||
                client.getSaleRepresentative() == null ||
                !client.getSaleRepresentative().getId()
                        .equals(salesRepresentative.getId())) {

            throw new RuntimeException(
                    "You are not authorized to delete this business.");
        }

        reviewRepository.deleteByBusiness_BusinessId(businessId);

        qrCodeRepository.deleteByBusiness_BusinessId(businessId);

        businessRepository.delete(business);

        return "Business deleted successfully.";
    }
}