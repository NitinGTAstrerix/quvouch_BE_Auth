package com.app.quvouch.dtos;

import com.app.quvouch.Models.Business;
import com.app.quvouch.Models.Provider;
import com.app.quvouch.Models.Role;
import lombok.*;

import java.time.Instant;
import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private UUID id;
    private String email;
    private String name;
    private String password;
    private String image;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
    private boolean enable = true;
    private Provider provider = Provider.LOCAL;
    private Set<Role> roles= new HashSet<>();
}
