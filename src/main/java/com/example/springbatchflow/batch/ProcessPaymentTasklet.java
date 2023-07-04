package com.example.springbatchflow.batch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.springbatchflow.repositories.TransferPaymentRepository;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ProcessPaymentTasklet implements Tasklet{

    @Autowired
    private TransferPaymentRepository transferPaymentRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        
        String transactionId = chunkContext.getStepContext().getJobParameters().get("transactionId").toString();
        log.info("------>the payment of transaction {x} is processed successfully",transactionId);
        transferPaymentRepository.updateTransactionStatus(true, transactionId);
        
        return RepeatStatus.FINISHED;
    }
    
}
