package com.whitelabel.ecommerce.service;

import com.whitelabel.ecommerce.dto.LoginRequest;
import com.whitelabel.ecommerce.dto.RegisterRequest;
import com.whitelabel.ecommerce.entity.User;
import com.whitelabel.ecommerce.exception.AuthenticationFailedException;
import com.whitelabel.ecommerce.exception.NotFoundException;
import com.whitelabel.ecommerce.exception.UserAlreadyPresentException;
import com.whitelabel.ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public String register(RegisterRequest request) {
        if(userRepository.findByEmail(request.email()).isPresent()){
            throw new UserAlreadyPresentException("Email already registered. Try with another email!");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(user);

        if(!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new AuthenticationFailedException("Authentication filed due to wrong credentials");
        }

        return jwtService.generateToken(request.email());
    }

    public String login(LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.email());

        if(optionalUser.isEmpty()){
            throw new NotFoundException("User not found with email " + request.email());
        }

        User user = optionalUser.get();

        if(!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new AuthenticationFailedException("Authentication filed due to wrong credentials");
        }

        return jwtService.generateToken(request.email());
    }
}

