package com.apikeygen.apikeygenerator.security;

import com.apikeygen.apikeygenerator.dto.LogRequestDTO;
import com.apikeygen.apikeygenerator.dto.LogResponseDTO;
import com.apikeygen.apikeygenerator.dto.RegRequestDTO;
import com.apikeygen.apikeygenerator.model.User;
import com.apikeygen.apikeygenerator.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public void register(RegRequestDTO request) {
        userRepository.findByEmail(request.email()).ifPresent(user -> {
            throw new IllegalStateException("Email already in use");
        });

        String passwordHash = passwordEncoder.encode(request.password());

        User user = new User(
                null,
                request.email(),
                passwordHash
        );

        userRepository.save(user);
    }

    public LogResponseDTO login(LogRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("User not found after successful authentication."));

        String token = tokenService.generateToken(user);
        return  new LogResponseDTO(token);
    }
}

