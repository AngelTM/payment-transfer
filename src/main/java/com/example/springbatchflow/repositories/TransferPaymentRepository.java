package com.example.springbatchflow.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.springbatchflow.entities.TransferPaymentEntity;

@Repository
public interface TransferPaymentRepository extends JpaRepository<TransferPaymentEntity,String>{
    
    @Modifying
    @Transactional
    @Query("update TransferPaymentEntity tpe SET tpe.isProcessed = ?1 where tpe.transactionId = ?2")
    void updateTransactionStatus(Boolean newValue, String transactionId);

    @Modifying
    @Transactional
    @Query("update TransferPaymentEntity tpe SET tpe.isProcessed = ?1, tpe.error = ?2 where tpe.transactionId = ?3")
    void updateTransactionStatusError(Boolean newValue, String error, String transactionId);

}
