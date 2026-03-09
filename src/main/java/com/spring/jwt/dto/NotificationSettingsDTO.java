package com.spring.jwt.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationSettingsDTO {

    @NotNull(message = "Email notification preference is required")
    private Boolean emailNotifications;

    @NotNull(message = "SMS notification preference is required")
    private Boolean smsNotifications;

    @NotNull(message = "Client assigned notification preference is required")
    private Boolean clientAssigned;

    @NotNull(message = "Deal updates notification preference is required")
    private Boolean dealUpdates;

    @NotNull(message = "System alerts notification preference is required")
    private Boolean systemAlerts;
}