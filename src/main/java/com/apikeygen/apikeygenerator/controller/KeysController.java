package com.apikeygen.apikeygenerator.controller;

import com.apikeygen.apikeygenerator.dto.NewKeyRequestDTO;
import com.apikeygen.apikeygenerator.dto.NewKeyResponseDTO;
import com.apikeygen.apikeygenerator.model.User;
import com.apikeygen.apikeygenerator.service.KeyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/keys")
public class KeysController {

    private KeyService keyService;

    public KeysController(KeyService keyService) {
        this.keyService = keyService;
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        } else {
            throw new IllegalStateException("User not authenticated or principal type is incorrect.");
        }
    }

    @PostMapping
    public ResponseEntity<NewKeyResponseDTO> createKey(@RequestBody NewKeyRequestDTO request) {
        User authenticatedUser = getAuthenticatedUser();
        String ownerID = authenticatedUser.id();

        NewKeyResponseDTO response = keyService.generateKey(ownerID, request.name());
        return ResponseEntity.status(201).body(response);
    }
}
