package com.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
public class JwtAuthenticationResponseDto {

    private String accessToken;
    private String tokenType = "Bearer";
    private String defaultCurrency;

    public JwtAuthenticationResponseDto(String accessToken, String defaultCurrency) {
        this.accessToken = accessToken;
        this.defaultCurrency = defaultCurrency;

    }



}