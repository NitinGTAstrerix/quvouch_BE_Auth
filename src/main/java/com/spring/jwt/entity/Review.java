package com.spring.jwt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @Column(name = "qr_code_id", nullable = false)
    private UUID qrCodeId;

    @Column(nullable = false)
    private Integer rating;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Column(name = "feedback_text", columnDefinition = "TEXT")
    private String feedbackText; // Only for ratings <= 3

    @Column(name = "feedback_category")
    private String feedbackCategory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewStatus status; // PUBLIC or INTERNAL [cite: 565]

    @Column(name = "redirect_url")
    private String redirectUrl;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public enum ReviewStatus {
        PUBLIC,
        INTERNAL
    }
}