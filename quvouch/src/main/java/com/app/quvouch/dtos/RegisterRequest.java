package com.app.quvouch.dtos;

import com.app.quvouch.Models.Provider;
import com.app.quvouch.Models.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class RegisterRequest {
    private String email;
    private String name;
    private String password;
    private String image;
    private boolean enable = true;
    private Set<Role> roles= new HashSet<>();
}
