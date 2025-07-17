package com.example.voomacrud.dto;

public record JwtTokenResponse(String accessToken,
                               String tokenType,
                               int expiresIn) {
}
