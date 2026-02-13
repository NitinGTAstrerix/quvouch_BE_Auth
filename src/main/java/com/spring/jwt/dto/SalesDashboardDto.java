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

    private long pendingClients;

    private long inactiveClients;

    private long activeQrCodes;
}
