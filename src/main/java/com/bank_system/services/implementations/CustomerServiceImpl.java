package com.bank_system.services.implementations;

import com.bank_system.dtos.customer.CreateUpdateCustomer;
import com.bank_system.dtos.response.ApiResponse;
import com.bank_system.exceptions.CustomException;
import com.bank_system.exceptions.NotFoundException;
import com.bank_system.models.Customer;
import com.bank_system.repositories.ICustomerRepository;
import com.bank_system.services.interfaces.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final ICustomerRepository customerRepository;

    @Override
    public ResponseEntity<ApiResponse<Customer>> createCustomer(CreateUpdateCustomer customerBody) {
        try {
            Customer customer = new Customer(customerBody.getEmail(), customerBody.getFirstName(), customerBody.getLastName(), customerBody.getMobile(), customerBody.getBalance(), customerBody.getDob());
            return ApiResponse.success("Customer created successfully!", HttpStatus.CREATED, customerRepository.save(customer));
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    @Override
    public ResponseEntity<ApiResponse<Customer>> updateCustomer(CreateUpdateCustomer customer, Long customerId) {
        try {
            Customer registeredCustomer = customerRepository.findById(customerId).orElseThrow(() -> new NotFoundException("Customer not found!"));
            registeredCustomer.setEmail(customer.getEmail());
            registeredCustomer.setFirstName(customer.getFirstName());
            registeredCustomer.setLastName(customer.getLastName());
            registeredCustomer.setDob(customer.getDob());
            registeredCustomer.setMobile(customer.getMobile());
            registeredCustomer.setBalance(customer.getBalance());

            return ApiResponse.success("Customer updated successfully!", HttpStatus.OK, customerRepository.save(registeredCustomer));
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    /**
     * @param customerId
     * @return
     */
    @Override
    public ResponseEntity<ApiResponse<Customer>> deleteCustomer(Long customerId) {
        try {
            Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new NotFoundException("Customer not found!"));
            customerRepository.delete(customer);

            return ApiResponse.success("Customer removed successfully!", HttpStatus.OK, customer);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    /**
     * @param customerId
     * @return
     */
    @Override
    public ResponseEntity<ApiResponse<Optional<Customer>>> getCustomer(Long customerId) {
        try {
            Optional<Customer> customer = customerRepository.findById(customerId);
            if (customer.isPresent()) {
                return ApiResponse.success("Customer retrieved successfully!", HttpStatus.OK, customer);
            }
            return ApiResponse.error("Customer not found!", HttpStatus.NOT_FOUND, customer);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    /**
     * @return
     */
    @Override
    public ResponseEntity<ApiResponse<Page<Customer>>> getAllCustomers(Pageable pageable) {
        try {
            Page<Customer> customers = customerRepository.findAll(pageable);
            if (customers.isEmpty()) {
                return ApiResponse.error("No customers are available!", HttpStatus.NOT_FOUND, customers);
            }
            return ApiResponse.success("All customers retrieved successfully!", HttpStatus.OK, customers);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }
}
