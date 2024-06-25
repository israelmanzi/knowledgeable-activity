package com.bank_system.models;

import com.bank_system.enumerations.transaction.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction extends Base {
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    private String account;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(nullable = false)
    private Date bankingDateTime;

    private int amount;

    @Column(name = "target_customer_id", nullable = true)
    private Long targetCustomerId;

    @Column(name = "target_account", nullable = true)
    private String targetAccount;

    public Transaction(Long customerId, String account, TransactionType type, Date bankingDateTime, int amount) {
        this.amount = amount;
        this.bankingDateTime = bankingDateTime;
        this.type = type;
        this.account = account;
        this.customerId = customerId;
    }
}
