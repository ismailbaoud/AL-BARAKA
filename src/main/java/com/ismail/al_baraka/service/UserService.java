package com.ismail.al_baraka.service;

import com.ismail.al_baraka.dto.account.response.AccountResponse;
import com.ismail.al_baraka.dto.user.request.UserRequest;

public interface UserService {
        public AccountResponse createUser(UserRequest request);
        public AccountResponse getUser(Long id);
        public AccountResponse updateUser(Long id,UserRequest request);
        public String deleteUser(Long id);
}
