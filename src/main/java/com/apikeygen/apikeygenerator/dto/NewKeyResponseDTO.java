package com.apikeygen.apikeygenerator.dto;

public record NewKeyResponseDTO(
    String rawKey,
    String keyVisual,
    String keyID
) {}
