package com.whitelabel.ecommerce.service;

import com.whitelabel.ecommerce.dto.UserResponse;
import com.whitelabel.ecommerce.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.whitelabel.ecommerce.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<UserResponse> getAllUsers(int pageNumber, int size) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        return userRepository.findAll(pageable).map(UserResponse::fromEntity);
    }
}
