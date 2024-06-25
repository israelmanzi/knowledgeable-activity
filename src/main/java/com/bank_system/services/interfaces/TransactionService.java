package com.bank_system.services.interfaces;

import com.bank_system.dtos.response.ApiResponse;
import com.bank_system.dtos.transaction.CreateTransaction;
import com.bank_system.exceptions.InsufficientBalanceException;
import com.bank_system.models.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TransactionService {
    public ResponseEntity<ApiResponse<Transaction>> createTransaction(CreateTransaction transaction) throws InsufficientBalanceException;

    public ResponseEntity<ApiResponse<Transaction>> getTransactionById(Long id);

    public ResponseEntity<ApiResponse<List<Transaction>>> getTransactionsByAccount(String account);

    ResponseEntity<ApiResponse<Page<Transaction>>> findAllTransactions(Pageable pageable);
}
