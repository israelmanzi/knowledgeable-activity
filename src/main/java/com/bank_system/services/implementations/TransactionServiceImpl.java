package com.bank_system.services.implementations;

import com.bank_system.dtos.response.ApiResponse;
import com.bank_system.dtos.transaction.CreateTransaction;
import com.bank_system.exceptions.CustomException;
import com.bank_system.exceptions.InsufficientBalanceException;
import com.bank_system.models.Customer;
import com.bank_system.models.Transaction;
import com.bank_system.repositories.ICustomerRepository;
import com.bank_system.repositories.ITransactionRepository;
import com.bank_system.services.interfaces.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private ICustomerRepository customerRepository;

    @Autowired
    private ITransactionRepository transactionRepository;

    @Transactional
    public ResponseEntity<ApiResponse<Transaction>> createTransaction(CreateTransaction transaction) throws InsufficientBalanceException {
        Customer customer = customerRepository.findById(transaction.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + transaction.getCustomerId()));
        Customer targetCustomer = transaction.getTargetCustomerId() != null ?
                customerRepository.findById(transaction.getTargetCustomerId())
                        .orElseThrow(() -> new RuntimeException("Target Customer not found with id: " + transaction.getTargetCustomerId()))
                : null;

        // Check transaction type and perform respective operation
        switch (transaction.getType()) {
            case WITHDRAW:
                performWithdraw(customer, transaction);
                break;
            case TRANSFER:
                performTransfer(customer, targetCustomer, transaction);
                break;
            case SAVING:
                performSaving(customer, transaction);
                break;
            default:
                throw new IllegalArgumentException("Invalid transaction type.");
        }

        // Save the transaction
        Transaction newTransaction = new Transaction(transaction.getCustomerId(), transaction.getTargetCustomerId(), transaction.getAccount(), transaction.getTargetAccount(), transaction.getType(), transaction.getBankingDateTime(), transaction.getAmount());
        return ApiResponse.success(transaction.getType().toString(), HttpStatus.OK, transactionRepository.save(newTransaction));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Transaction>> getTransactionById(Long id) {
        return ApiResponse.success("Transaction fetched successfully!", HttpStatus.OK, transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id)));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<List<Transaction>>> getTransactionsByAccount(String account) {
        return ApiResponse.success("Transactions fetched successfully!", HttpStatus.OK, transactionRepository.findByAccount(account));
    }

    @Transactional
    public ResponseEntity<ApiResponse<Page<Transaction>>> findAllTransactions(Pageable pageable) {
        try {
            Page<Transaction> transactions = transactionRepository.findAll(pageable);
            if (transactions.isEmpty()) {
                return ApiResponse.error("No transactions available!", HttpStatus.NOT_FOUND, transactions);
            }
            return ApiResponse.success("Successfully fetched all transactions", HttpStatus.OK, transactions);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    public void performWithdraw(Customer customer, CreateTransaction transaction) throws InsufficientBalanceException {
        try {
            if (customer.getBalance() < transaction.getAmount()) {
                throw new InsufficientBalanceException("Insufficient balance for withdrawal.");
            }
            customer.setBalance(customer.getBalance() - transaction.getAmount());
            customerRepository.save(customer);

            // Update transaction details
            transaction.setBankingDateTime(new Date());
            transaction.setAccount(customer.getAccount());

            ApiResponse.success("Withdraw successful!", HttpStatus.OK, transaction);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    public void performTransfer(Customer sourceCustomer, Customer targetCustomer, CreateTransaction transaction) throws InsufficientBalanceException {
        try {
            if (sourceCustomer.getBalance() < transaction.getAmount()) {
                throw new InsufficientBalanceException("Insufficient balance for transfer.");
            }
            sourceCustomer.setBalance(sourceCustomer.getBalance() - transaction.getAmount());
            targetCustomer.setBalance(targetCustomer.getBalance() + transaction.getAmount());

            customerRepository.save(sourceCustomer);
            customerRepository.save(targetCustomer);

            // Update transaction details
            transaction.setBankingDateTime(new Date());
            transaction.setAccount(sourceCustomer.getAccount());
            transaction.setTargetAccount(targetCustomer.getAccount());

            ApiResponse.success("Transfer successful!", HttpStatus.OK, transaction);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    public void performSaving(Customer customer, CreateTransaction transaction) {
        try {
            customer.setBalance(customer.getBalance() + transaction.getAmount());
            customerRepository.save(customer);

            // Update transaction details
            transaction.setBankingDateTime(new Date());
            transaction.setAccount(customer.getAccount());

            ApiResponse.success("Saving successful!", HttpStatus.OK, transaction);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }
}
