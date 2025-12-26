package com.ismail.al_baraka.service.impliment;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.ismail.al_baraka.Exception.customExceptions.AuthenticationException;
import com.ismail.al_baraka.config.Service.JwtService;
import com.ismail.al_baraka.dto.auth.request.LoginRequest;
import com.ismail.al_baraka.dto.auth.response.LoginResponse;
import com.ismail.al_baraka.mapper.UserMapper;
import com.ismail.al_baraka.repository.UserRepository;
import com.ismail.al_baraka.service.AuthService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    
    @Override
    @Transactional
    public LoginResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getFullName(),
                request.getPassword()
            )
        );
        var user = userRepository.findByFullName(request.getFullName()).orElseThrow(()->new AuthenticationException());
        var jwtToken = jwtService.generateToken(user);
        return LoginResponse.builder()
                            .token(jwtToken)
                            .user(userMapper.toDto(user))
                            .build();
    }
}
