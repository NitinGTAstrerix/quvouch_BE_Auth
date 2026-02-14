package com.spring.jwt.dto;

import com.spring.jwt.entity.Business.BusinessStatus;
import jakarta.validation.constraints.NotNull;

public class SalesClientStatusDto {

    @NotNull(message = "Status is required")
    private BusinessStatus status;

    public BusinessStatus getStatus() {
        return status;
    }

    public void setStatus(BusinessStatus status) {
        this.status = status;
    }
}