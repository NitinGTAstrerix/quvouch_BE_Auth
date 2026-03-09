package com.spring.jwt.controller;

import com.spring.jwt.dto.NotificationSettingsDTO;
import com.spring.jwt.service.NotificationSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings/notifications")
@RequiredArgsConstructor
@Tag(
        name = "Notification Settings",
        description = "APIs for managing user notification preferences"
)
public class NotificationSettingsController {

    private final NotificationSettingsService service;

    @Operation(summary = "Get notification settings", description = "Fetch the notification preferences for the currently logged-in user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification settings fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping
    public NotificationSettingsDTO getSettings() {
        return service.getSettings();
    }

    @Operation(summary = "Update notification settings", description = "Update notification preferences such as email, SMS, client assignment, deal updates, and system alerts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification settings updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PutMapping
    public String updateSettings(@Valid @RequestBody NotificationSettingsDTO request) {
        service.updateSettings(request);
        return "Notification settings updated";
    }
}