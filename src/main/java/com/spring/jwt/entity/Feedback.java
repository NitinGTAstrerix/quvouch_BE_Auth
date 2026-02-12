package com.spring.jwt.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String message;

    private Integer rating;

    //  TIMESTAMP ADDED HERE
    private LocalDateTime createdAt;

    // FEEDBACK MAPPED TO BUSINESS
    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;
}
