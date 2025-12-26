package com.ismail.al_baraka.model;

import java.time.LocalDateTime;
import java.util.List;

import com.ismail.al_baraka.model.enums.OperationType;
import com.ismail.al_baraka.model.enums.Status;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    
    @Enumerated(EnumType.STRING)
    private OperationType operationType;
    private Double amount;
    
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime validatedAt;
    private LocalDateTime executedAt;

    @ManyToOne
    @JoinColumn(name = "account_source_id")
    private Account accountSource;

    @ManyToOne
    @JoinColumn(name = "account_destination_id")
    private Account accountDestination;

    @OneToMany(mappedBy = "operation")
    private List<Document> documents;
}
