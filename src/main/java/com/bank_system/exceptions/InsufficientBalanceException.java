package com.bank_system.exceptions;

import java.io.Serial;

// Custom Exception
public class InsufficientBalanceException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InsufficientBalanceException(String message) {
        super(message);
    }

    public InsufficientBalanceException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
