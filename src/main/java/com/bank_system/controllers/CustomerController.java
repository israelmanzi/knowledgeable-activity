package com.bank_system.controllers;

import com.bank_system.dtos.customer.CreateUpdateCustomer;
import com.bank_system.dtos.response.ApiResponse;
import com.bank_system.models.Customer;
import com.bank_system.services.interfaces.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Customer>> createCustomer(@Valid @RequestBody CreateUpdateCustomer customer) {
        return customerService.createCustomer(customer);
    }

    @PutMapping("/update/{customerId}")
    public ResponseEntity<ApiResponse<Customer>> updateCustomer(@Valid @RequestBody CreateUpdateCustomer customer, @PathVariable Long customerId) {
        return customerService.updateCustomer(customer, customerId);
    }

    @DeleteMapping("/delete/{customerId}")
    public ResponseEntity<ApiResponse<Customer>> deleteCustomer(@PathVariable Long customerId) {
        return customerService.deleteCustomer(customerId);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse<Optional<Customer>>> getCustomer(@PathVariable Long customerId) {
        return customerService.getCustomer(customerId);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Page<Customer>>> getAllCustomers(@RequestParam(name = "page", defaultValue = "0") int pageNo, @RequestParam(name = "size", defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.Direction.ASC,        "id");
        return customerService.getAllCustomers(pageable);
    }
}
