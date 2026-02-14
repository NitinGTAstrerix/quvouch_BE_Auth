package com.spring.jwt.service;

import com.spring.jwt.dto.SaleRepresentativeInfo;
import com.spring.jwt.dto.UserDTO;
import com.spring.jwt.entity.User;

import java.util.List;

public interface AdminService {
    public List<UserDTO> getMyClients();
    public User getCurrentUserProfile();
    public List<SaleRepresentativeInfo> getAllSaleRepsWithClientCount();
    public UserDTO getSaleRepresentativeById(Long id);

}
