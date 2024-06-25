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

    @Column(name = "target_customer_id", nullable = true)
    private Long targetCustomerId;
    private String account;
    @Null
    private String targetAccount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(nullable = false)
    private Date bankingDateTime;

    private int amount;
}
