package com.example.voomacrud.controller;

import com.example.voomacrud.dto.JwtTokenRequestDto;
import com.example.voomacrud.dto.JwtTokenResponse;
import com.example.voomacrud.dto.TsqResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/demo")
@CrossOrigin(origins = "http://localhost:8082")
@RequiredArgsConstructor
public class DemoController {
    private final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30";

    @PostMapping(value = "/get-token", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<JwtTokenResponse> getToken(
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("grant_type") String grantType,
            @RequestParam(value = "scope", required = false) String scope) {
        if (clientId == null || clientSecret == null || grantType == null) {
            return ResponseEntity.badRequest().build();
        }
        if (!"client_credentials".equals(grantType)) {
            return ResponseEntity.status(400).body(null); // Invalid grant_type
        }
        String tokenType = "Bearer";
        int expiresIn = 3600;
        JwtTokenResponse tokenResponse = new JwtTokenResponse(JWT_TOKEN, tokenType, expiresIn);
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping("/transactions/{transactionId}")
    public ResponseEntity<?> getTransactionById(@PathVariable String transactionId,
                                                             @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        // Check if the Authorization header is present
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(null); // Unauthorized
        }

        // Extract the token and validate it
        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
        if (!JWT_TOKEN.equals(token)) {
            return ResponseEntity.status(403).body("Invalid Jwt token"); // Forbidden
        }

        // Simulate fetching a transaction by ID
        if (transactionId == null || transactionId.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if( transactionId.equals("12345")) {
            TsqResponseDto tsqResponse = new TsqResponseDto(
                    "12345",
                    100.00,
                    "03542790002811",
                    "SWAZI MATATA",
                    "2547174674274",
                    "CASH",
                    "Payment to Vendor",
                    "OTC",
                    LocalDateTime.now(),
                    "CR"
            );
            return ResponseEntity.ok(tsqResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
