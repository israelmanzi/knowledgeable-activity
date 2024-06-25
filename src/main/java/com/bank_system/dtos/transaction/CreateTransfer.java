package com.bank_system.dtos.transaction;

import com.bank_system.enumerations.transaction.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateTransfer {
    @NotNull
    @Positive
    private Long customerId;

    @NotNull
    @Positive
    private Long targetCustomerId;

    @NotNull
    @Schema(description = "Type of the transaction", defaultValue = "TRANSFER")
    private TransactionType type = TransactionType.TRANSFER;

    @Pattern(regexp = "^ACC\\d{13,16}$", message = "Invalid account number, should start with 'ACC' followed by 14-16 digits")
    @NotNull
    private String account;

    @Pattern(regexp = "^ACC\\d{13,16}$", message = "Invalid target account number, should start with 'ACC' followed by 14-16 digits")
    @NotNull
    private String targetAccount;

    @NotNull
    @PositiveOrZero(message = "Amount must be a positive number")
    private int amount;
}
