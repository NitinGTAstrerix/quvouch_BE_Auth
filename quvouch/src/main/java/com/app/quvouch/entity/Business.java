package com.app.quvouch.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID businessId;

    private String businessName;

    private String businessType;

    private String businessAddress;

    private Long phoneNumber;

    private Instant createdAt = Instant.now();

    private Instant updatedAt = Instant.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    public void onCreate()
    {
        Instant now = Instant.now();
        if (createdAt == null) createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void onUpdate()
    {

        updatedAt = Instant.now();
    }
}
