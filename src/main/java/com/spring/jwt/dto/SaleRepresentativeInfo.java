package com.spring.jwt.dto;

public record SaleRepresentativeInfo(
        Integer id,
        String firstName,
        String lastName,
        String email,
        Long mobileNumber,
        Long totalClients
) {}

