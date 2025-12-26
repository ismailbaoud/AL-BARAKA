package com.ismail.al_baraka.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ismail.al_baraka.model.Document;

public interface DocumentRepository extends JpaRepository<Document , Long> {
}