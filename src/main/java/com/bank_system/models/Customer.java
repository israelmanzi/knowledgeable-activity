package com.bank_system.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
public class Customer extends Base {

    @Column(unique = true, nullable = false)
    private String email;

    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String mobile;

    @Column(unique = true, nullable = false)
    private String account;

    private int balance;
    private Date dob;

    public Customer(String email, String firstName, String lastName, String mobile, int amount, Date dob) {
        setEmail(email);
        setFirstName(firstName);
        setLastName(lastName);
        setMobile(mobile);
        setBalance(amount);
        setDob(dob);
        generateAccountNumber();
    }

    private void generateAccountNumber() {
        setAccount("ACC" + System.currentTimeMillis());
    }
}

