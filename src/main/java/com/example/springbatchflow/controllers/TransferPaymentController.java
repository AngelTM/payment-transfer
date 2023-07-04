package com.example.springbatchflow.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springbatchflow.controllers.request.TransferPaymentDTO;
import com.example.springbatchflow.entities.TransferPaymentEntity;
import com.example.springbatchflow.repositories.TransferPaymentRepository;

@RestController
@RequestMapping("v1/payment")
public class TransferPaymentController {
    
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Autowired
    private TransferPaymentRepository transferPaymentRepository;

    @PostMapping("/transfer")
    public ResponseEntity<Map<String,Object>> transferPayment(@RequestBody TransferPaymentDTO transferPaymentDTO) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException{
        String transactionID = UUID.randomUUID().toString();

        TransferPaymentEntity transferPaymentEntity = TransferPaymentEntity.builder()
                              .transactionId(transactionID)
                              .avaiableBalance(transferPaymentDTO.getAvaiableBalance())
                              .amountPaid(transferPaymentDTO.getAmountPaid())
                              .isEnabled(transferPaymentDTO.getIsEnabled())
                              .isProcessed(false)
                              .build();
        transferPaymentRepository.save(transferPaymentEntity);
        
        JobParameters jobParameters = new JobParametersBuilder()
                        .addString("id", UUID.randomUUID().toString())
                        .addString("transactionId", transactionID)
                        .toJobParameters();

        jobLauncher.run(job, jobParameters);

        Map<String,Object> httpResponse = new HashMap<>();
        httpResponse.put("TransactionId", transactionID);
        httpResponse.put("message", "Transaction received");
        return ResponseEntity.ok(httpResponse);
    }
}
