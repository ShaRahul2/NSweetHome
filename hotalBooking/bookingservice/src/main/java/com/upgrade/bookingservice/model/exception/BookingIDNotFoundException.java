package com.upgrade.bookingservice.model.exception;

public class BookingIDNotFoundException extends  RuntimeException {
    public BookingIDNotFoundException(String message) {  super(message);  }
}
