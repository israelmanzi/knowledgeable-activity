package com.bank_system.services.interfaces;

import com.bank_system.dtos.auth.AuthResponse;
import com.bank_system.dtos.auth.LoginDTO;
import com.bank_system.dtos.auth.RegisterUserDTO;
import com.bank_system.dtos.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    public ResponseEntity<ApiResponse<AuthResponse>> login(LoginDTO signInDTO);

    public ResponseEntity<ApiResponse<AuthResponse>> register(RegisterUserDTO registerUserDTO);
}
