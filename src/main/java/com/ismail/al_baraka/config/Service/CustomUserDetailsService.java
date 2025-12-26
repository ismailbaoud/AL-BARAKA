package com.ismail.al_baraka.config.Service;


import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ismail.al_baraka.model.User;
import com.ismail.al_baraka.model.enums.AuthProvider;
import com.ismail.al_baraka.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String fullName) throws UsernameNotFoundException {
        User user = userRepository.findByFullName(fullName)
                .orElseThrow(() -> new UsernameNotFoundException(fullName + " NOT FOUND"));

        return new CustomUserDetails(
                user.getId(),
                user.getFullName(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}

