package com.apikeygen.apikeygenerator.service;

import com.apikeygen.apikeygenerator.dto.NewKeyResponseDTO;
import com.apikeygen.apikeygenerator.model.Key;
import com.apikeygen.apikeygenerator.repository.ApikeyRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

@Service
public class KeyService {

    private final ApikeyRepository apikeyRepository;
    private final PasswordEncoder passwordEncoder;

    private static final int KEY_LENGTH_BYTES = 32;
    private static final long DEFAULT_VALIDITY_DAYS= 365;

    public KeyService(ApikeyRepository apikeyRepository, PasswordEncoder passwordEncoder) {
        this.apikeyRepository = apikeyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public NewKeyResponseDTO generateKey(String ownerID, String name) {
        String rawKey = generateRandomKey();
        String keyHash = passwordEncoder.encode(rawKey);
        int visualLength = Math.min(4, rawKey.length());
        String keyVisual = (rawKey.substring(rawKey.length() - visualLength));

        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(DEFAULT_VALIDITY_DAYS * 24 * 60 * 60);

        Key key = new Key(
                null,
                name,
                ownerID,
                keyHash,
                keyVisual,
                true,
                now,
                expiresAt
        );

        Key savedKey = apikeyRepository.save(key);
        return new NewKeyResponseDTO(
                rawKey,
                savedKey.keyVisual(),
                savedKey.id()
        );
    }

    private String generateRandomKey() {
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[KEY_LENGTH_BYTES];
        random.nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    public Optional<Key> validateKey(String rawKey) {
        return apikeyRepository.findAll().stream()
                .filter(Key::enabled)
                .filter(key -> key.expiresAt().isAfter(Instant.now()))
                .filter(key -> passwordEncoder.matches(rawKey, key.keyHash()))
                .findFirst();
    }
}
