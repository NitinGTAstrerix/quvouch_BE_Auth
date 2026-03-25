package com.spring.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDashboardDTO {

    private long totalSalesReps;
    private long totalClients;
    private long totalReviews;
    private double platformRevenue;

}