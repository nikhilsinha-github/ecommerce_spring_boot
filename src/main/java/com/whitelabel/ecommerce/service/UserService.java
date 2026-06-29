package com.whitelabel.ecommerce.service;

import com.whitelabel.ecommerce.entity.User;
import org.springframework.stereotype.Service;
import com.whitelabel.ecommerce.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
