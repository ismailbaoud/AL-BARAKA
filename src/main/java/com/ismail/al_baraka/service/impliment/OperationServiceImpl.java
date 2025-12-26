package com.ismail.al_baraka.service.impliment;

import java.time.LocalDateTime;
import java.util.List;


import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import com.ismail.al_baraka.Exception.customExceptions.AccountUserNotFoundException;
import com.ismail.al_baraka.Exception.customExceptions.BalanceNotEnoughException;
import com.ismail.al_baraka.Exception.customExceptions.OperationNotFoundException;
import com.ismail.al_baraka.dto.operation.request.OperationRequest;
import com.ismail.al_baraka.dto.operation.response.OperationResponse;
import com.ismail.al_baraka.helper.SecurityUtil;
import com.ismail.al_baraka.mapper.OperationMapper;
import com.ismail.al_baraka.model.Account;
import com.ismail.al_baraka.model.Operation;
import com.ismail.al_baraka.model.enums.OperationType;
import com.ismail.al_baraka.model.enums.Status;
import com.ismail.al_baraka.repository.AccountRepository;
import com.ismail.al_baraka.repository.OperationRepository;
import com.ismail.al_baraka.service.OperationService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService{

    private final AccountRepository accountRepository;
    private final OperationMapper operationMapper;
    private final OperationRepository operationRepository;
    
    @Override
    public OperationResponse createOperation(OperationRequest request) {
        OperationResponse operationResponse = new OperationResponse();
        if(request.getOperationType().equals(OperationType.DEPOSIT)) operationResponse = depositOperation(request);
        if(request.getOperationType().equals(OperationType.WITHDRAWAL)) operationResponse = withdrawalOperation(request);
        if(request.getOperationType().equals(OperationType.TRANSFER)) operationResponse = transferOperation(request);
        return operationResponse;
    }
    
    @Transactional
    private OperationResponse depositOperation(OperationRequest request) {
        Long id = SecurityUtil.currentUser().getId();
        Account account = accountRepository.findByOwner_IdAndOwner_ActiveTrue(id).orElseThrow();
        Double newAmount = account.getBalance()+request.getAmount();
        Operation operation = new Operation();
        operation.setCreatedAt(LocalDateTime.now());
        operation.setAccountSource(account);
        operation.setOperationType(OperationType.DEPOSIT);
        operation.setAmount(request.getAmount());
        if(request.getAmount() <= 10_000) {
            operation.setStatus(Status.APPROVED);
            account.setBalance(newAmount);
            accountRepository.save(account);
        }else {
            operation.setStatus(Status.PANDING);
        }
        operationRepository.save(operation);
        return operationMapper.toDto(operation);
    }
    
    @Transactional
    private OperationResponse withdrawalOperation(OperationRequest request) {
        Long id = SecurityUtil.currentUser().getId();

        Account account = accountRepository.findByOwner_IdAndOwner_ActiveTrue(id).orElseThrow(()-> new AccountUserNotFoundException());
        if(account.getBalance()<request.getAmount()) {
            throw new BalanceNotEnoughException();
        }
        Double newAmount = account.getBalance()-request.getAmount();
        Operation operation = new Operation();
        operation.setAmount(request.getAmount());
        operation.setCreatedAt(LocalDateTime.now());
        operation.setAccountSource(account);
        operation.setOperationType(OperationType.WITHDRAWAL);
        if(request.getAmount() <= 10_000) {
            operation.setStatus(Status.APPROVED);
            account.setBalance(newAmount);
            accountRepository.save(account);
        }else {
            operation.setStatus(Status.PANDING);
        }
        operationRepository.save(operation);
        return operationMapper.toDto(operation);
    }

    @Transactional
    private OperationResponse transferOperation(OperationRequest request) {
        Long sourceId = SecurityUtil.currentUser().getId();
        Long distinationId = request.getAccountDestination();

        Account sourceAccount = accountRepository.findByOwner_IdAndOwner_ActiveTrue(sourceId).orElseThrow(() -> new AccountUserNotFoundException());
        Account distinationAccount = accountRepository.findByOwner_IdAndOwner_ActiveTrue(distinationId).orElseThrow(()-> new AccountUserNotFoundException());
        if(sourceAccount.getBalance()<request.getAmount()) {
            throw new BalanceNotEnoughException();
        }
        Double sourceNewAmount = sourceAccount.getBalance()-request.getAmount();
        Double destNewAmount = distinationAccount.getBalance()+request.getAmount();
        Operation operation = new Operation();
        operation.setAmount(request.getAmount());
        operation.setCreatedAt(LocalDateTime.now());
        operation.setAccountSource(sourceAccount);
        operation.setAccountDestination(distinationAccount);
        operation.setOperationType(OperationType.TRANSFER);
        operation.setStatus(Status.APPROVED);
        if(request.getAmount() <= 10_000) {
            operation.setStatus(Status.APPROVED);
            sourceAccount.setBalance(sourceNewAmount);
            distinationAccount.setBalance(destNewAmount);
            accountRepository.save(sourceAccount);
            accountRepository.save(distinationAccount);
        }else {
            operation.setStatus(Status.PANDING);
        }
        operationRepository.save(operation);
        return operationMapper.toDto(operation);
    }

    @Override
    public @Nullable List<OperationResponse> getOperationList() {
        return operationRepository.findAll().stream().map(a -> operationMapper.toDto(a)).toList();
    }

    @Override
    public @Nullable List<OperationResponse> getPandingOperationsList() {
        return operationRepository.findAll().stream().filter(a -> a.getStatus().equals(Status.PANDING)).map(a -> operationMapper.toDto(a)).toList();
    }

    @Transactional
    @Override
    public OperationResponse approveOperation(Long opId) {
    Operation operation = operationRepository.findById(opId)
            .orElseThrow(() -> new OperationNotFoundException());

    Account sourceAccount = accountRepository.findById(operation.getAccountSource().getId())
            .orElseThrow(() -> new AccountUserNotFoundException("source account not found"));

    if (operation.getOperationType() == OperationType.TRANSFER) {
        Account destAccount = accountRepository.findById(operation.getAccountDestination().getId())
                .orElseThrow(() -> new AccountUserNotFoundException("destination account not found"));

        sourceAccount.setBalance(sourceAccount.getBalance() - operation.getAmount());
        destAccount.setBalance(destAccount.getBalance() + operation.getAmount());

        accountRepository.save(sourceAccount);
        accountRepository.save(destAccount);
    } else if (operation.getOperationType() == OperationType.DEPOSIT) {
        sourceAccount.setBalance(sourceAccount.getBalance() + operation.getAmount());
        accountRepository.save(sourceAccount);
    } else if (operation.getOperationType() == OperationType.WITHDRAWAL) {
        sourceAccount.setBalance(sourceAccount.getBalance() - operation.getAmount());
        accountRepository.save(sourceAccount);
    } else {
        throw new RuntimeException("Unsupported operation type");
    }

    operation.setStatus(Status.APPROVED);
    operation.setValidatedAt(LocalDateTime.now());
    operation.setExecutedAt(LocalDateTime.now());

    operationRepository.save(operation);

    return operationMapper.toDto(operation);
}

    @Transactional
    @Override
    public OperationResponse rejectOperation(Long opId) {
        Operation operation = operationRepository.findById(opId).orElseThrow(()-> new OperationNotFoundException());
        operation.setStatus(Status.REJECTED);
        operation.setValidatedAt(LocalDateTime.now());
        operation.setExecutedAt(LocalDateTime.now());
        operationRepository.save(operation);
        return operationMapper.toDto(operation);    
    }
}
