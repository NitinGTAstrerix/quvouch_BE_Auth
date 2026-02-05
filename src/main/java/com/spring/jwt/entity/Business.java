package com.spring.jwt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "business_seq")
    @SequenceGenerator(name = "business_seq", sequenceName = "business_sequence", allocationSize = 1, initialValue = 10000)
    private Integer businessId;

    private String businessName;
    private String businessType;
    private String address;
    private Long phoneNumber;

    private String businessEmail;

    @Enumerated(EnumType.STRING)
    private BusinessStatus status = BusinessStatus.ACTIVE;

    private Instant createdAt= Instant.now();
    private Instant updatedAt = Instant.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QrCode> qrCode;

    @PrePersist
    private void onCreate()
    {
        if (createdAt == null)
        {
            createdAt = Instant.now();
        }
        updatedAt = Instant.now();
    }

    @PreUpdate
    private void onUpdate()
    {
        if (updatedAt == null)
        {
            updatedAt = Instant.now();
        }
    }
    public enum BusinessStatus {
        ACTIVE,
        INACTIVE,
        PENDING
    }

}
