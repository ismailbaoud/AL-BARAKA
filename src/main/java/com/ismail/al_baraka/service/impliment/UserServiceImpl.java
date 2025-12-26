package com.ismail.al_baraka.service.impliment;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ismail.al_baraka.Exception.customExceptions.AccountUserNotFoundException;
import com.ismail.al_baraka.Exception.customExceptions.AccountdisactivatedException;
import com.ismail.al_baraka.dto.account.response.AccountResponse;
import com.ismail.al_baraka.dto.user.request.UserRequest;
import com.ismail.al_baraka.helper.AccountNumber;
import com.ismail.al_baraka.mapper.AccountMapper;
import com.ismail.al_baraka.mapper.UserMapper;
import com.ismail.al_baraka.model.Account;
import com.ismail.al_baraka.model.User;
import com.ismail.al_baraka.model.enums.Role;
import com.ismail.al_baraka.repository.AccountRepository;
import com.ismail.al_baraka.repository.UserRepository;
import com.ismail.al_baraka.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AccountNumber accountNumber;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    @Transactional
    public AccountResponse createUser(UserRequest request) {
        User userReq = userMapper.toEntity(request);
        userReq.setActive(true);
        userReq.setCreatedAt(LocalDateTime.now());
        userReq.setRole(Role.CLIENT);
        userReq.setPassword(passwordEncoder.encode(userReq.getPassword()));
        User user = userRepository.save(userReq);
        Account account = Account.builder()
                        .balance(0.0)
                        .owner(user)
                        .accountNumber(accountNumber.get())
                        .build();
        Account savedAcc = accountRepository.save(account);

        return accountMapper.toDto(savedAcc);
    }

    @Override
    @Transactional
    public AccountResponse getUser(Long id) {
        Account account = accountRepository.findByOwner_IdAndOwner_ActiveTrue(id).orElse(null);
        if(account == null) {
            throw new AccountUserNotFoundException();
        }else if(!account.getOwner().isActive()) {
            throw new AccountdisactivatedException();
        }
        return accountMapper.toDto(account);
    }

    @Override
    @Transactional
    public AccountResponse updateUser(Long id, UserRequest request) {
        User user = userRepository.findById(id).orElseThrow(()-> new AccountUserNotFoundException("user not found"));
        user.setFullName(request.getFullName());
        userRepository.save(user);
        Account account = accountRepository.findByOwner_IdAndOwner_ActiveTrue(id).orElse(null);
        if(account == null) {
            throw new AccountUserNotFoundException();
        }else if(!account.getOwner().isActive()) {
            throw new AccountdisactivatedException();
        }
        return accountMapper.toDto(account);
    }

    @Override
    @Transactional
    public String deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new AccountUserNotFoundException("user not found"));
        user.setActive(false);
        userRepository.save(user);
        return "deleted succufuly";
    }

}
