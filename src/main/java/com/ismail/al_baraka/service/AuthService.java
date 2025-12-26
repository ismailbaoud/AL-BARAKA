package com.ismail.al_baraka.service;

import com.ismail.al_baraka.dto.auth.request.LoginRequest;
import com.ismail.al_baraka.dto.auth.response.LoginResponse;

public interface AuthService {
        public LoginResponse authenticate(LoginRequest request);
}
