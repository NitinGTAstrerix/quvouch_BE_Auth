package com.spring.jwt.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QrCode {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String qrValue;

    @Enumerated(EnumType.STRING)
    private QrStatus status = QrStatus.ACTIVE;

    private Instant createdAt = Instant.now();

    // Each QR belongs to one Business
    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;

    public enum QrStatus {
        ACTIVE,
        INACTIVE
    }
}
