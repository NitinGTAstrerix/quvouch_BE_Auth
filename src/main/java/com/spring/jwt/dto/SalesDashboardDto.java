package com.spring.jwt.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesDashboardDto {

    private long totalClients;

    private long activeClients;

    private long inactiveClients;

    private long activeQrCodes;
}
