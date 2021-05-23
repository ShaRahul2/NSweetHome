package com.upgrade.paymentservice.exception;

public class TransactionIdNotFoundException extends  RuntimeException {

    public TransactionIdNotFoundException(String message) {
        super(message);
    }
}
