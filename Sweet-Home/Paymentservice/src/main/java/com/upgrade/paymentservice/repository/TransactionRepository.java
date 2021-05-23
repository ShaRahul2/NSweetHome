package com.upgrade.paymentservice.repository;

import com.upgrade.paymentservice.model.TransactionDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionDetailEntity, Serializable> {
}
