package com.ismail.al_baraka.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ismail.al_baraka.model.Account;

public interface AccountRepository extends JpaRepository<Account , Long> {

    Optional<Account> findByOwner_IdAndOwner_ActiveTrue(Long userId);
}