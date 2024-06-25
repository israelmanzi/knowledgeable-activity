package com.bank_system.services.interfaces;

import com.bank_system.dtos.customer.CreateUpdateCustomer;
import com.bank_system.dtos.response.ApiResponse;
import com.bank_system.models.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface CustomerService {
    public ResponseEntity<ApiResponse<Customer>> createCustomer(CreateUpdateCustomer customer);

    public ResponseEntity<ApiResponse<Customer>> updateCustomer(CreateUpdateCustomer customer, Long customerId);

    public ResponseEntity<ApiResponse<Customer>> deleteCustomer(Long customerId);

    public ResponseEntity<ApiResponse<Optional<Customer>>> getCustomer(Long customerId);

    public ResponseEntity<ApiResponse<Page<Customer>>> getAllCustomers(Pageable pageable);
}
