package com.bank_system.dtos.auth;

import com.bank_system.models.User;
import lombok.Data;

@Data
public class AuthResponse {
    private User user;
    private TokensResponse tokens;

    public AuthResponse(String accessToken, User user) {
        this.tokens = new TokensResponse(accessToken);
        this.user = user;
    }
}
