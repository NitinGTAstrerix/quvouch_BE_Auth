package com.spring.jwt.service.impl;

import com.spring.jwt.dto.*;
import com.spring.jwt.entity.Business;
import com.spring.jwt.entity.QrCode;
import com.spring.jwt.entity.Review;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.BaseException;
import com.spring.jwt.exception.BusinessNotFound;
import com.spring.jwt.mapper.BusinessMapper;
import com.spring.jwt.repository.BusinessRepository;
import com.spring.jwt.repository.QrCodeRepository;
import com.spring.jwt.repository.ReviewRepository;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.dto.BusinessDashboardDto;
import com.spring.jwt.dto.RatingDistributionDto;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import com.spring.jwt.service.BusinessService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Slf4j
@Service
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository businessRepository;
    private final ReviewRepository reviewRepository;
    private final QrCodeRepository qrCodeRepository;
    private final BusinessMapper mapper;
    private final UserRepository userRepository;


    @Override
    public BusinessResponseDto createBusiness(BusinessRequestDto dto) {

        User loggedUser = getCurrentUserProfile();

        if (loggedUser == null) {
            throw new RuntimeException("Logged user not found");
        }

        boolean isAdmin = loggedUser.getRoles().stream()
                .anyMatch(r -> r.getName().equals("ADMIN"));

        boolean isSaleRep = loggedUser.getRoles().stream()
                .anyMatch(r -> r.getName().equals("SALE_REPRESENTATIVE"));

        boolean isClient = loggedUser.getRoles().stream()
                .anyMatch(r -> r.getName().equals("CLIENT"));

        if (!isAdmin && !isSaleRep && !isClient) {
            throw new RuntimeException("You are not authorized to create business");
        }

        User client;

        // ✅ If ADMIN → can select any client
        if (isAdmin) {

            client = userRepository.findById(dto.getClientId())
                    .orElseThrow(() -> new RuntimeException("Client not found"));

        }
        // ✅ If SALE REP → only for assigned clients
        else if (isSaleRep) {

            client = userRepository.findById(dto.getClientId())
                    .orElseThrow(() -> new RuntimeException("Client not found"));

            if (client.getSaleRepresentative() == null ||
                    !client.getSaleRepresentative().getId().equals(loggedUser.getId())) {

                throw new RuntimeException("You can create business only for your assigned clients");
            }
        }
        // ✅ If CLIENT → create only for self
        else {

            client = loggedUser;

            // Optional: prevent client from passing another clientId
            if (dto.getClientId() != null &&
                    !dto.getClientId().equals(loggedUser.getId())) {

                throw new RuntimeException("Client can create business only for themselves");
            }
        }

        Business business = new Business();

        business.setBusinessName(dto.getBusinessName());
        business.setBusinessType(dto.getBusinessType());
        business.setAddress(dto.getAddress());
        business.setPhoneNumber(dto.getPhoneNumber());
        business.setBusinessEmail(dto.getBusinessEmail());
        business.setStatus(Business.BusinessStatus.ACTIVE);

        business.setUser(client);

        Business saved = businessRepository.save(business);

        return mapper.toBusiness(saved);
    }

    @Override
    public BusinessResponseDto getBusinessById(Integer businessId) {
        Business business = businessRepository.findById(businessId).orElseThrow(() -> new UsernameNotFoundException("Business is Not Found"));
        return mapper.toBusiness(business);
    }

    @Override
    public BusinessResponseDto getBusinessByOwn() {
        Integer id = getCurrentUserProfile().getId();
        Business business = businessRepository.findByUser_Id(id).orElseThrow(() -> new BusinessNotFound("Business not found"));
        return mapper.toBusiness(business);
    }

    @Override
    public List<BusinessResponseDto> getAllBusiness() {
        List<Business> allBusiness = businessRepository.findAll();

        return allBusiness.stream().map(mapper::toBusiness).toList();
    }

    @Override
    public Page<BusinessResponseDto> getAllBusinessByPageNumber(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Business> all = businessRepository.findAll(pageable);
        return all.map(mapper::toBusiness);
    }

    @Override
    public User getCurrentUserProfile() {
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
    public BusinessResponseDto updateBusiness(Integer businessId, BusinessRequestDto businessRequestDto) {
        Business business = businessRepository.findById(businessId).orElseThrow(() -> new BusinessNotFound("Business is not found"));

        if (businessRequestDto.getBusinessName() != null){
            business.setBusinessName(businessRequestDto.getBusinessName());
        }
        if(businessRequestDto.getBusinessType() != null)
        {
            business.setBusinessType(businessRequestDto.getBusinessType());
        }
        if (businessRequestDto.getPhoneNumber()!=null)
        {
            business.setPhoneNumber(businessRequestDto.getPhoneNumber());
        }
        if (businessRequestDto.getAddress() != null)
        {
            business.setAddress(businessRequestDto.getAddress());
        }
        Business updateBusiness = businessRepository.save(business);

        return mapper.toBusiness(updateBusiness);
    }

    @Override
    public BusinessDashboardDto getDashboardData(Integer businessId) {

        // 1️⃣ Get Business
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found"));

        // 2️⃣ Review Stats
        ReviewStatsDTO stats =
                reviewRepository.getReviewStatistics(businessId);

        Long totalReviews = (stats != null && stats.getTotalReviews() != null)
                ? stats.getTotalReviews()
                : 0L;

        Double averageRating = (stats != null && stats.getAverageRating() != null)
                ? stats.getAverageRating()
                : 0.0;

        // 3️⃣ Total QR Scans (using your existing method)
        Long totalScans = qrCodeRepository.getTotalScans(business);
        if (totalScans == null) {
            totalScans = 0L;
        }

        // 4️⃣ Active QR Codes (using your existing method)
        Long activeQrCodes =
                qrCodeRepository.countByBusinessAndActiveTrue(business);

        if (activeQrCodes == null) {
            activeQrCodes = 0L;
        }

        // 5️⃣ Rating Distribution
        List<Object[]> distribution =
                reviewRepository.getRatingDistribution(businessId);

        List<RatingDistributionDto> ratingList = distribution.stream()
                .map(obj -> new RatingDistributionDto(
                        (Integer) obj[0],
                        (Long) obj[1]))
                .toList();

        return new BusinessDashboardDto(
                totalReviews,
                averageRating,
                totalScans,
                activeQrCodes,
                ratingList
        );
    }

    @Override
    public byte[] exportReviewsAsCsv(Integer businessId) {

        List<Review> reviews =
                reviewRepository.findByBusiness_BusinessId(businessId);

        StringBuilder sb = new StringBuilder();

        sb.append("Customer Name,Email,Phone,Rating,Feedback,Category,Status,Created At\n");

        for (Review r : reviews) {

            sb.append(safe(r.getCustomerName())).append(",");
            sb.append(safe(r.getCustomerEmail())).append(",");
            sb.append(safe(r.getCustomerPhone())).append(",");
            sb.append(r.getRating()).append(",");
            sb.append("\"").append(safe(r.getFeedbackText())).append("\"").append(",");
            sb.append(safe(r.getFeedbackCategory())).append(",");
            sb.append(r.getStatus()).append(",");
            sb.append(r.getCreatedAt()).append("\n");
        }

        return sb.toString().getBytes();
    }

    @Override
    public byte[] exportReviewsAsPdf(Integer businessId) {

        List<Review> reviews = reviewRepository.findByBusiness_BusinessId(businessId);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Reviews Report"));
            document.add(new Paragraph(" "));

            Table table = new Table(4);

            table.addHeaderCell("Customer");
            table.addHeaderCell("Rating");
            table.addHeaderCell("Feedback Text");
            table.addHeaderCell("Date");

            for (Review review : reviews) {
                table.addCell(review.getCustomerName());
                table.addCell(String.valueOf(review.getRating()));
                table.addCell(review.getFeedbackText());
                table.addCell(review.getCreatedAt().toString());
            }

            document.add(table);
            document.close();

            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to export PDF", e);
        }
    }

    private String safe(String value) {
        return value == null ? "" : value.replace(",", " ");
    }



    @Override
    public String generateShareLink(Integer businessId) {
        return "https://quvouch.com/reviews/public/" + businessId;
    }

    @Override
    public List<MonthlyAnalyticsDTO> getMonthlyAnalytics(Integer businessId) {

        return reviewRepository.getMonthlyAnalytics(businessId);
    }

    @Override
    public UrlResource downloadQrCode(Integer businessId) {

        QrCode qrCode = qrCodeRepository.findByBusiness_BusinessId(businessId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("QR Code not found"));

        try {

            Path tempFile = Files.createTempFile("business-qr-", ".png");
            Files.write(tempFile, qrCode.getQrImage());

            return new UrlResource(tempFile.toUri());

        } catch (Exception e) {
            throw new RuntimeException("Failed to load QR code", e);
        }
    }

    @Override
    public List<ReviewResponseDto> getMyBusinessReviews(
            Integer rating,
            String keyword) {

        User user = getCurrentUserProfile();

        List<Review> reviews;

        if (rating != null) {
            reviews = reviewRepository
                    .findByBusiness_UserAndRating(user, rating);
        }
        else if (keyword != null && !keyword.isBlank()) {
            reviews = reviewRepository
                    .findByBusiness_UserAndFeedbackTextContainingIgnoreCase(user, keyword);
        }
        else {
            reviews = reviewRepository
                    .findByBusiness_User(user);
        }

        return reviews.stream()
                .map(review -> ReviewResponseDto.builder()
                        .reviewId(review.getId())
                        .customerName(review.getCustomerName())
                        .rating(review.getRating())
                        .comment(review.getFeedbackText())
                        .location(review.getBusiness().getBusinessName())
                        .createdAt(review.getCreatedAt())
                        .build()
                )
                .toList();
    }

    @Override
    @Transactional
    public String deleteBusiness(Integer businessId) {

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
            e.printStackTrace();   // 🔥 THIS WILL SHOW REAL ERROR
            throw e;
        }
    }
}