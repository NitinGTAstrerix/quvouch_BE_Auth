package com.app.quvouch.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;
@Data
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
public class Role {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(unique = true, nullable = false)
    private String roleName;
}
