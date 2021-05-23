package com.upgrade.bookingservice.model;

import lombok.*;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Getter @Setter
@ToString
/**
 * This class only used Reading
 * */
@Component
public class Transaction {
    private int transactionId;
    private String paymentMode;
    private int bookingId;
    private String upiId;
    private String cardNumber;


}
