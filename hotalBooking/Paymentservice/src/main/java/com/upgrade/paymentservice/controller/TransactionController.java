package com.upgrade.paymentservice.controller;

import com.upgrade.paymentservice.model.TransactionDetailEntity;
import com.upgrade.paymentservice.service.TransactionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Slf4j
public class TransactionController {

    private TransactionService transactionService;

    @ApiOperation(
            consumes = "application/json",
            produces = "application/json",
            value = "Create New Record of Payment Transaction",
            response = TransactionDetailEntity.class
    )
    @ApiResponses( value = {
            @ApiResponse(code = 201, message = "Payment Info. Successfully Create"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @PostMapping(value = "/transaction", produces = {"application/json"}, consumes =  {"application/json"})
    public TransactionDetailEntity createPaymentTransaction(@RequestBody TransactionDetailEntity transactionDetailEntity) {
        transactionDetailEntity = transactionService.savePaymentTransaction(transactionDetailEntity);
        log.info("Payment Succufully Paid : " + transactionDetailEntity);
        return  transactionDetailEntity;
    }

    @ApiOperation(
            consumes = "application/json",
            produces = "application/json",
            value = "fetch Transaction Records By Id's",
            response = TransactionDetailEntity.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved Transaction"),
            @ApiResponse(code = 404, message = "Transaction Not Found")
    })
    @GetMapping("/transaction/{id}")
    public TransactionDetailEntity findByTransactionId(@PathVariable("id") int id) {
        return transactionService.getTransactionRecordeById(id);
    }

}
