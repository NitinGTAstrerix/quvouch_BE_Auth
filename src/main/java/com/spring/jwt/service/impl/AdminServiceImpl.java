package com.spring.jwt.service.impl;

import com.spring.jwt.dto.SaleRepresentativeInfo;
import com.spring.jwt.dto.UserDTO;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.BaseException;
import com.spring.jwt.mapper.UserMapper;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @PreAuthorize("hasAuthority('ADMIN')")
    public List<SaleRepresentativeInfo> getAllSaleRepsWithClientCount() {

        List<User> saleReps = userRepository.findAllSaleRepresentatives();

        return saleReps.stream()
                .map(rep -> {

                    Long clientCount =
                            userRepository.countBySaleRepresentative_Id(rep.getId());

                    return new SaleRepresentativeInfo(
                            rep.getId(),
                            rep.getFirstName(),
                            rep.getLastName(),
                            rep.getEmail(),
                            rep.getMobileNumber(),
                            clientCount
                    );
                })
                .toList();
    }


    @PreAuthorize("hasAuthority('SALE_REPRESENTATIVE')")
    public List<UserDTO> getMyClients() {

        User currentUser = getCurrentUserProfile(); // from SecurityContext

        List<User> clients =
                userRepository.findBySaleRepresentative_Id(currentUser.getId());

        return clients.stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Override
    public User getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
        {
            throw new BaseException(String.valueOf(HttpStatus.UNAUTHORIZED.value()),"User is not Authenticated");
        }
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        if (user == null)
        {
            throw new UsernameNotFoundException("User is not found");
        }

        return user;
    }
}
