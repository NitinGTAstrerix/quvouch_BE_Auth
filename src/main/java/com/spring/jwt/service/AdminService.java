package com.spring.jwt.service;

import com.spring.jwt.dto.*;
import com.spring.jwt.entity.User;

import java.util.List;

public interface AdminService {
    public List<UserDTO> getMyClients();
    public User getCurrentUserProfile();
    public List<SaleRepresentativeInfo> getAllSaleRepsWithClientCount();
    public UserDTO getSaleRepresentativeById(Long id);

    List<ClientListDTO> getAllClients();

    ClientDetailsDTO getClientDetails(Integer businessId);

    List<AdminQrCodeDTO> getAllQrCodes();
}