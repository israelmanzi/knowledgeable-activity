package com.bank_system.dtos.transaction;

import com.bank_system.enumerations.transaction.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateTransaction {
    @NotNull
    @Positive
    private Long customerId;

    @Positive
    private Long targetCustomerId;

    @NotNull
    private TransactionType type;

    @Pattern(regexp = "^\\+?[0-9]{14,16}$", message = "Invalid account number, should be between 14-16 digits")
    @NotNull
    private String account;

    @Pattern(regexp = "^\\+?[0-9]{14,16}$", message = "Invalid account number, should be between 14-16 digits")
    private String targetAccount;

    @NotNull
    @PositiveOrZero(message = "Amount must be a positive number")
    private int amount;

    private Date bankingDateTime;
}
