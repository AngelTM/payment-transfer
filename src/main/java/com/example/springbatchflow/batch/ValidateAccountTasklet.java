package com.example.springbatchflow.batch;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.springbatchflow.entities.TransferPaymentEntity;
import com.example.springbatchflow.repositories.TransferPaymentRepository;

public class ValidateAccountTasklet implements Tasklet{


    @Autowired
    private TransferPaymentRepository transferPaymentRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    
            Boolean filterIsApproved = true;

            String transactionId = chunkContext.getStepContext()
                .getStepExecution()
                .getJobParameters()
                .getString("transactionId");

            TransferPaymentEntity transferPayment = transferPaymentRepository.findById(transactionId).orElseThrow();
            if (!transferPayment.getIsEnabled()) {
                //Error cause the account is inactive
                filterIsApproved = false;
                chunkContext.getStepContext()
                            .getStepExecution()
                            .getJobExecution()
                            .getExecutionContext()
                            .put("message", "inactive account error");
            }
            if(transferPayment.getAmountPaid() > transferPayment.getAvaiableBalance()){
                filterIsApproved = false;
                chunkContext.getStepContext()
                            .getStepExecution()
                            .getJobExecution()
                            .getExecutionContext()
                            .put("message", "insufficient balance error");
            }

            ExitStatus exitStatus =null;
            if (filterIsApproved) {
                exitStatus = new ExitStatus("VALID");
                contribution.setExitStatus(exitStatus);
            }else{
                exitStatus = new ExitStatus("INVALID");
                contribution.setExitStatus(exitStatus);
            }

            return RepeatStatus.FINISHED;
    }
    
}
