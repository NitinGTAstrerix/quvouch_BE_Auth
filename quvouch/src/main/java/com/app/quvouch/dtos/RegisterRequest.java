package com.app.quvouch.dtos;

import com.app.quvouch.Models.Provider;
import com.app.quvouch.Models.Role;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RegisterRequest {
    private String email;
    private String name;
    private String password;
    private String image;
    private boolean enable = true;
}
