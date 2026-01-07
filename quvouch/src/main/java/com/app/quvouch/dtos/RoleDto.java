package com.app.quvouch.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RoleDto {
    private UUID id;
    private String roleName;
}