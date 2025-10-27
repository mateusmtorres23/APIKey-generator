package com.apikeygen.apikeygenerator.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "keys")
public record Key(
        @Id
        String id,
        String name,
        String ownerId,
        String keyHash,
        String keyVisual,
        boolean enabled,
        Instant createdAt,
        Instant expiresAt
) {}
