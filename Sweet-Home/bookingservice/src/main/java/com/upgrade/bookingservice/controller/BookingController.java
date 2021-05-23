package com.upgrade.bookingservice.controller;

import com.upgrade.bookingservice.model.BookingInfoEntity;
import com.upgrade.bookingservice.model.Transaction;
import com.upgrade.bookingservice.service.BookingService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class Handle are All Request of BookingService
 * @author Rahul Sharma
 * @since
 * */
@RestController
@AllArgsConstructor
@Slf4j
public class BookingController {

    private BookingService bookingService;

    @ApiOperation(
            consumes = "application/json",
            produces = "application/json",
            value = "Create New Booking Record",
            response = BookingInfoEntity.class
    )
    @ApiResponses( value = {
            @ApiResponse(code = 201, message = "Booking Order Successfully Create"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @PostMapping(value = "/booking", consumes = {"application/json"}, produces = {"application/json"})
    public BookingInfoEntity createBookingRecorde(@RequestBody BookingInfoEntity bookingInfoEntity) {
        bookingInfoEntity = bookingService.createBooking(bookingInfoEntity);
        log.info("Booking Record Successfully : " + bookingInfoEntity);
        return  bookingInfoEntity;
    }

    /*@ApiOperation(
            consumes = "application/json",
            value = "Transaction Payment create",
            response = Void.class
    )
    @ApiResponses( value = {
            @ApiResponse(code = 201, message = "Booking Order Successfully Create"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @PostMapping(value = "/transaction")
    public void transaction(@RequestBody Transaction transaction) {
        bookingService.bookingTransaction(transaction);
        log.info("Payment Successfully");
    }*/

    @ApiOperation(
            consumes = "application/json",
            produces = "application/json",
            value = "Transaction Payment create",
            response = BookingInfoEntity.class
    )
    @ApiResponses( value = {
            @ApiResponse(code = 201, message = "Get Booking Order With Payment Details"),
            @ApiResponse(code = 404, message = "Booking Oder Not Found "),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @PostMapping(value = "/booking/{id}/transaction", consumes = {"application/json"}, produces = {"application/json"})
    public BookingInfoEntity fetchBookingById(@PathVariable("id") int id, @RequestBody Transaction transaction) {
        return bookingService.getBookingById(id, transaction);
    }
}
