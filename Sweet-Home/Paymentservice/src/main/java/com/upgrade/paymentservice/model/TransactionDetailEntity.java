package com.upgrade.paymentservice.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity(name = "transaction")
@NoArgsConstructor
@Getter @Setter
@ToString
public class TransactionDetailEntity {
    /**
     * It refers to the transaction Id and is used to uniquely identify a transaction
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(notes = "It refers to the transaction Id and is used to uniquely identify a transaction")
    private Integer transactionId;

    /**
     * It refers to the user's mode of payment which can have values either as 'upi' or ' card'.
     * **/
    @ApiModelProperty(notes = "It refers to the user's mode of payment which can have values either as 'upi' or ' card'.")
    private String paymentMode;

    /**
     * It refers to the bookingId which we receive from the 'hotel booking service' when the payment service is called
     * **/
    @NotNull(message = "Booking Id May Not Be Blank")
    @ApiModelProperty(notes = "It refers to the bookingId which we receive from the 'hotel booking service' when the payment service is called")
    private int bookingId;

    /**
     * If the user's mode of payment is 'upi', he has to provide the upiId and cardNumber must be null.
     * **/
    @ApiModelProperty(notes = "If the user's mode of payment is 'upi', he has to provide the upiId and cardNumber must be null.")
    private String upiId;

    /**
     * If the user's mode of payment is 'card', he has to provide the cardNumber and upiId must be null.
     * **/
    @ApiModelProperty(notes = "If the user's mode of payment is 'card', he has to provide the cardNumber and upiId must be null")
    private String cardNumber;

    public TransactionDetailEntity(String paymentMode, int bookingId, String upiId, String cardNumber) {
        this.paymentMode = paymentMode;
        this.bookingId = bookingId;
        this.upiId = upiId;
        this.cardNumber = cardNumber;
    }
}
