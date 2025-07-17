package com.example.voomacrud.dto;

public record JwtTokenRequestDto(String clientId,
                                 String clientSecret,
                                 String grantType,
                                 String scope) {
}
