package com.spring.jwt.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientResponseDto {

    private Integer clientId;
    private String firstName;
    private String lastName;
    private String email;
    private Long mobileNumber;
}