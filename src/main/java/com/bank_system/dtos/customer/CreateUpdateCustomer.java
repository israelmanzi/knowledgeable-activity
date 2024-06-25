package com.bank_system.dtos.customer;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateUpdateCustomer {
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid mobile number")
    @NotNull
    private String mobile;
    @NotNull
    @Email(message = "Email address is invalid", flags = {Pattern.Flag.CASE_INSENSITIVE})
    private String email;
    @NotNull
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String firstName;
    @NotNull
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String lastName;
    @NotNull
    @Past(message = "Date of birth must be in the past")
    private Date dob;
    @NotNull
    @PositiveOrZero(message = "Balance must be a positive number")
    private int balance;
}
