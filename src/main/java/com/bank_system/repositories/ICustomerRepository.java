package com.bank_system.repositories;

import com.bank_system.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICustomerRepository extends JpaRepository<Customer, Long>{
    Optional<Customer> findByAccount(String account);
}
