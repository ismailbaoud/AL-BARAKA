package com.ismail.al_baraka.config.Service;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.ismail.al_baraka.helper.AccountNumber;
import com.ismail.al_baraka.model.Account;
import com.ismail.al_baraka.model.User;
import com.ismail.al_baraka.model.enums.AuthProvider;
import com.ismail.al_baraka.model.enums.Role;
import com.ismail.al_baraka.repository.AccountRepository;
import com.ismail.al_baraka.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountNumber accountNumber;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        AuthProvider provider = AuthProvider.valueOf(registrationId.toUpperCase());

        User user = userRepository.findByEmailAndProvider(email, provider)
                .orElseGet(() -> createUser(oAuth2User, provider));

        String token = jwtService.generateToken(user);

        response.setContentType("application/json");
        response.getWriter().write("{\"token\": \"%s\"}".formatted(token));
        response.getWriter().flush();
        }

        private User createUser(OAuth2User oAuth2User, AuthProvider provider) {
            User user = User.builder()
                    .fullName(oAuth2User.getAttribute("name"))
                    .role(Role.CLIENT)
                    .email(oAuth2User.getAttribute("email"))
                    .provider(provider)
                    .build();
            if(provider.equals(AuthProvider.KEYCLOAK)) {
                    user.setRole(Role.AGENT_BANCAIRE);
            }
            User savedUser = userRepository.save(user);
            Account account = Account.builder()
                    .accountNumber(accountNumber.get())
                    .balance(0.0)
                    .owner(savedUser)
                    .build();
            accountRepository.save(account);
            return savedUser;
        }
}