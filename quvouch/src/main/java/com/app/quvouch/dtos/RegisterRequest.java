package com.app.quvouch.dtos;

import lombok.*;

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
