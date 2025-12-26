package com.ismail.al_baraka.helper;

import java.nio.file.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.ismail.al_baraka.config.Service.CustomUserDetails;

@Component
public class SecurityUtil {

    public static CustomUserDetails currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Not authenticated");
        }

        return (CustomUserDetails) auth.getPrincipal();
    }
}

