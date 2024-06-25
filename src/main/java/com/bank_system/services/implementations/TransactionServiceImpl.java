package com.bank_system.services.implementations;

import com.bank_system.dtos.response.ApiResponse;
import com.bank_system.dtos.transaction.CreateTransaction;
import com.bank_system.dtos.transaction.CreateTransfer;
import com.bank_system.exceptions.CustomException;
import com.bank_system.exceptions.NotFoundException;
import com.bank_system.models.Customer;
import com.bank_system.models.Message;
import com.bank_system.models.Transaction;
import com.bank_system.repositories.ICustomerRepository;
import com.bank_system.repositories.IMessageRepository;
import com.bank_system.repositories.ITransactionRepository;
import com.bank_system.services.interfaces.TransactionService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private ICustomerRepository customerRepository;

    @Autowired
    private ITransactionRepository transactionRepository;

    @Autowired
    private IMessageRepository messageRepository;

    private final EmailService emailService;

    @Transactional
    public ResponseEntity<ApiResponse<Transaction>> createTransaction(CreateTransaction transaction) {
        try {
            // Find the customer
            Customer customer = customerRepository.findByIdAndAccount(transaction.getCustomerId(), transaction.getAccount()).orElseThrow(() -> new NotFoundException("Customer not found!"));

            // Check transaction type and perform respective operation
            switch (transaction.getType()) {
                case WITHDRAW:
                    performWithdraw(customer, transaction);
                    break;
                case SAVING:
                    performSaving(customer, transaction);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid transaction type.");
            }

            // Save the transaction
            Transaction newTransaction = new Transaction(transaction.getCustomerId(), transaction.getAccount(), transaction.getType(), new Date(), transaction.getAmount());
            return ApiResponse.success(transaction.getType().toString(), HttpStatus.OK, transactionRepository.save(newTransaction));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST, e);
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<Transaction>> transfer(CreateTransfer transaction) {
        try {
            // Find the customer
            Customer customer = customerRepository.findByIdAndAccount(transaction.getCustomerId(), transaction.getAccount()).orElseThrow(() -> new NotFoundException("Customer not found!"));

            // Find the target customer if provided
            Customer targetCustomer = customerRepository.findByIdAndAccount(transaction.getTargetCustomerId(), transaction.getTargetAccount()).orElseThrow(() -> new NotFoundException("Target Customer not found!"));

            // Check for sufficient balance
            if (customer.getBalance() < transaction.getAmount()) {
                throw new IllegalArgumentException("Insufficient balance for transfer!");
            }

            // Check if source and target are the same customer or account
            if (customer.getId().equals(targetCustomer.getId()) || customer.getAccount().equals(targetCustomer.getAccount())) {
                throw new IllegalArgumentException("Cannot perform a self-transfer!");
            }

            // Perform the balance transfer
            int transactionAmount = transaction.getAmount();
            int newOriginBalance = customer.getBalance() - transactionAmount;
            int newDestBalance = targetCustomer.getBalance() + transactionAmount;

            customer.setBalance(newOriginBalance);
            targetCustomer.setBalance(newDestBalance);

            // Save updated customer information
            customerRepository.save(customer);
            customerRepository.save(targetCustomer);

            Transaction newTransaction = new Transaction(transaction.getCustomerId(), transaction.getAccount(), transaction.getType(), new Date(), transaction.getAmount(), transaction.getTargetCustomerId(), transaction.getTargetAccount());

            emailService.sendTransferEmail(customer, targetCustomer, transaction.getAmount());

            Message message = new Message();
            message.setCustomer(customer);
            message.setMessage("Transferred " + transaction.getAmount() + " to " + customer.getFirstName() + " " + targetCustomer.getLastName());

            messageRepository.save(message);

            return ApiResponse.success(transaction.getType().toString(), HttpStatus.OK, transactionRepository.save(newTransaction));
        } catch (NotFoundException e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.NOT_FOUND, e);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST, e);
        } catch (Exception e) {
            return ApiResponse.error("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }


    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Transaction>> getTransactionById(Long id) {
        return ApiResponse.success("Transaction fetched successfully!", HttpStatus.OK, transactionRepository.findById(id).orElseThrow(() -> new NotFoundException("Transaction not found with id: " + id)));
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

    public void performWithdraw(Customer customer, CreateTransaction transaction) throws CustomException {
        try {
            if (customer.getBalance() < transaction.getAmount()) {
                throw new NotFoundException("Insufficient balance for withdrawal.");
            }
            customer.setBalance(customer.getBalance() - transaction.getAmount());
            customerRepository.save(customer);

            // Update transaction details
            transaction.setAccount(customer.getAccount());

            emailService.sendWithdrawEmail(customer, transaction.getAmount());

            Message message = new Message();
            message.setCustomer(customer);
            message.setMessage("Withdrew " + transaction.getAmount() + " from the account");
            messageRepository.save(message);

            ApiResponse.success("Withdraw successful!", HttpStatus.OK, transaction);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }


    public void performSaving(Customer customer, CreateTransaction transaction) throws CustomException {
        try {
            customer.setBalance(customer.getBalance() + transaction.getAmount());
            customerRepository.save(customer);

            // Update transaction details
            transaction.setAccount(customer.getAccount());

            emailService.sendSavingEmail(customer, transaction.getAmount());

            Message message = new Message();
            message.setCustomer(customer);
            message.setMessage("Saved " + transaction.getAmount() + " into the account");
            messageRepository.save(message);

            ApiResponse.success("Saving successful!", HttpStatus.OK, transaction);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }
}
