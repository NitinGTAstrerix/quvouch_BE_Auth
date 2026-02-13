package com.spring.jwt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "userProfile")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userProfileId;
    private String name;
    private String lastName;
    private String dateOfBirth;
    private String address;
    private String studentcol;
    private String studentcol1;
    private String studentClass;

    private Integer userId;

}
