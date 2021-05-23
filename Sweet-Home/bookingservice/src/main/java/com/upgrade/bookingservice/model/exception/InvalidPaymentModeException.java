package com.upgrade.bookingservice.model.exception;

public class InvalidPaymentModeException extends RuntimeException {

    public InvalidPaymentModeException(String message) {  super(message); }
}
