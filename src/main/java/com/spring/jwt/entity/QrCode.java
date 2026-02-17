package com.spring.jwt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "qr_code")
public class QrCode {

    @Id
    private String id;

    private String qrLink;

    @Column(name = "location_label")
    private String location;

    private Integer scanCount = 0;

    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = true)
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by")
    private User assignedBy;

    private LocalDateTime assignedAt;

    @Enumerated(EnumType.STRING)
    private QrStatus status = QrStatus.UNASSIGNED;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Lob
    @Column(name = "qr_image", columnDefinition = "LONGBLOB")
    private byte[] qrImage;

    @Column(name = "qr_code_path")
    private String qrCodePath;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if (this.status == null) {
            this.status = QrStatus.UNASSIGNED;
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum QrStatus {

        UNASSIGNED,
        ASSIGNED,
        ACTIVE,
        INACTIVE
    }
}
