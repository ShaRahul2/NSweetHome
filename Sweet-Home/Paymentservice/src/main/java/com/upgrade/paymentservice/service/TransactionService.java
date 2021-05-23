package com.upgrade.paymentservice.service;

import com.upgrade.paymentservice.model.TransactionDetailEntity;
import com.upgrade.paymentservice.exception.TransactionIdNotFoundException;
import com.upgrade.paymentservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public TransactionDetailEntity savePaymentTransaction(TransactionDetailEntity transactionDetailEntity) {
        return transactionRepository.save(transactionDetailEntity);
    }

    public TransactionDetailEntity getTransactionRecordeById(int id) {
        Optional<TransactionDetailEntity> optionalTransactionDetailEntity = transactionRepository.findById(id);
        if (! optionalTransactionDetailEntity.isPresent()) {
            throw new TransactionIdNotFoundException("Transaction Not Found");
        }
        return optionalTransactionDetailEntity.get();
     }
}
