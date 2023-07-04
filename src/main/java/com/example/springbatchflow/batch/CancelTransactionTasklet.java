package com.example.springbatchflow.batch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.springbatchflow.repositories.TransferPaymentRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CancelTransactionTasklet implements Tasklet{


    @Autowired
    private TransferPaymentRepository transferPaymentRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        
        String transactionId = chunkContext.getStepContext().getJobParameters().get("transactionId").toString();
        String errorMessage = chunkContext.getStepContext()
                                          .getStepExecution()
                                          .getJobExecution()
                                          .getExecutionContext()
                                          .getString("message");
        
        log.info("------>The transaction could not be processed for the following reason: ".concat(errorMessage));
        transferPaymentRepository.updateTransactionStatusError(true, errorMessage, transactionId);
        
        return RepeatStatus.FINISHED;
    }
    
}
