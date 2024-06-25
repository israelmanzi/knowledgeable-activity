package com.bank_system.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "messages")
public class Message extends Base {
    @Column(name = "message")
    private String message;
    @ManyToOne
    private Customer customer;
}
