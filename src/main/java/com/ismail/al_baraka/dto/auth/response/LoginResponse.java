package  com.ismail.al_baraka.dto.auth.response;

import com.ismail.al_baraka.dto.user.response.UserResponse;

import lombok.Builder;

@Builder
public record LoginResponse( 
    String token, 
    UserResponse user
) {}
