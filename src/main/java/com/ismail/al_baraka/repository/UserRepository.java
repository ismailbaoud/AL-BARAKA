package com.ismail.al_baraka.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.ismail.al_baraka.model.User;
import com.ismail.al_baraka.model.enums.AuthProvider;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByFullName(String fullName);

    Optional<User> findByEmailAndProviderIsNull(String email);
    Optional<User> findByEmailAndProvider(String email,AuthProvider  provider);

    Optional<User> findByEmail(String email);
}