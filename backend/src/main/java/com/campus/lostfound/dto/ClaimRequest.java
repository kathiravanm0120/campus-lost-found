package com.campus.lostfound.dto;

import jakarta.validation.constraints.NotBlank;

public record ClaimRequest(
        @NotBlank String message,
        String verificationAnswer
) {}
