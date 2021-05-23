package com.upgrade.bookingservice.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name= "booking")
@NoArgsConstructor
@Getter @Setter
@ToString
@JsonPropertyOrder({ "bookingId", "fromDate","toDate","aadharNumber","roomNumbers", "roomPrice","transactionId","bookedOn"})
/**
 * @author Rahul Sharma
 * @since
 * */
public class BookingInfoEntity {
    /**
     * It refers to the "BookingId" of the user and is used to uniquely identify a booking
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(notes ="It refers to the BookingId of the user and is used to uniquely identify a booking")
    private Integer bookingId;

    /**
     * It refers to the date from which the user is looking for the room
     * */
    @ApiModelProperty(notes ="It refers to the date from which the user is looking for the room")
    private LocalDate  fromDate;

    /**
     * It refers to the date untill when the user requires room
     * */
    @ApiModelProperty(notes ="It refers to the date untill when the user requires room")
    private LocalDate toDate;

    /***
     * aadhar number of the user. It helps to uniquely identify the user.
     */
    @ApiModelProperty(notes ="aadhar number of the user. It helps to uniquely identify the user.")
    private String aadharNumber;

    /***
     * It refers to the number of rooms required by user
     */
    @ApiModelProperty(notes ="It refers to the number of rooms required by user")
    private Integer numOfRooms;

    /**
     * It refers to the total price of the allocated rooms for the requested days.
     * Default value of a single room is Rs.1000, so if a user requests for 2 rooms for 2 days, then roomPrice will be Rs.4000.
     * */
    @ApiModelProperty(notes ="It refers to the total price of the allocated rooms for the requested days.\n" +
            " Default value of a single room is Rs.1000, so if a user requests for 2 rooms for 2 days, then roomPrice will be Rs.4000.")
    @NotNull(message = "Room Price  May Not be Null Or Empty.")
    private Integer roomPrice;

    /**
     * It refers to the transactionId which we get from the payment service.
     * '0' which represents that no transaction has happened so far for this booking
     * */
    @ApiModelProperty(notes ="It refers to the transactionId which we get from the payment service.\n" +
            " '0' which represents that no transaction has happened so far for this booking")
    @NaturalId(mutable=true)
    private Integer transactionId = 0;

    /**
     * It refers to the current date
     * */
    @ApiModelProperty(notes ="It refers to the current date")
    private LocalDate bookedOn;

    /**
     * It refers to, how many Room are Available
     * */
    @ApiModelProperty(notes ="It refers to, how many Room are Available")
    private String roomNumbers;

    /**
     * It refer to the current date.
     * */
    public LocalDateTime getBookedOn() { return LocalDateTime.now();}
}
