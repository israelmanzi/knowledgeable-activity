package com.bank_system.controllers;


import com.bank_system.dtos.response.ApiResponse;
import com.bank_system.dtos.transaction.CreateTransaction;
import com.bank_system.dtos.transaction.CreateTransfer;
import com.bank_system.exceptions.CustomException;
import com.bank_system.exceptions.InsufficientBalanceException;
import com.bank_system.models.Transaction;
import com.bank_system.services.implementations.MailSenderService;
import com.bank_system.services.implementations.TransactionServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class TransactionController {
    private final TransactionServiceImpl transactionService;
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Transaction>> createTransaction(@Valid @RequestBody CreateTransaction transaction) {
        try {
            return transactionService.createTransaction(transaction);
        } catch (CustomException e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST, e);
        } catch (Exception e) {
            return ApiResponse.error("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<Transaction>> transfer(@Valid @RequestBody CreateTransfer transaction) {
        try {
            return transactionService.transfer(transaction);
        } catch (CustomException e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST, e);
        } catch (Exception e) {
            return ApiResponse.error("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Transaction>> getTransactionById(@PathVariable Long id) {
        try {
            return transactionService.getTransactionById(id);
        } catch (Exception e) {
            return ApiResponse.error(e.toString(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/account/{account}")
    public ResponseEntity<ApiResponse<List<Transaction>>> getTransactionsByAccount(@PathVariable String account) {
        return transactionService.getTransactionsByAccount(account);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Page<Transaction>>> findAllTransactions(@RequestParam(name = "page", defaultValue = "0") int pageNo, @RequestParam(name = "size", defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return transactionService.findAllTransactions(pageable);
    }
}
