package com.example.springbatchflow.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.springbatchflow.batch.CancelTransactionTasklet;
import com.example.springbatchflow.batch.ProcessPaymentTasklet;
import com.example.springbatchflow.batch.SendNotificationTasklet;
import com.example.springbatchflow.batch.ValidateAccountTasklet;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;


    @Bean
    public ValidateAccountTasklet validateAccountTasklet(){
        return new ValidateAccountTasklet();
    }

    @Bean
    public ProcessPaymentTasklet processPaymentTasket(){
        return new ProcessPaymentTasklet();
    }

    @Bean
    public CancelTransactionTasklet cancelTransactionTasklet(){
        return new CancelTransactionTasklet();
    }

    @Bean
    public SendNotificationTasklet sendNotificationTasklet(){
        return new SendNotificationTasklet();
    }

    @Bean
    @JobScope
    public Step validateAccount(){
        return stepBuilderFactory.get("validateAccount")
                .tasklet(validateAccountTasklet())
                .build();
    }

    @Bean
    public Step processPayment(){
        return stepBuilderFactory.get("processPayment")
                .tasklet(processPaymentTasket())
                .build();
    }

    @Bean
    public Step cancelTransaction(){
        return stepBuilderFactory.get("cancelTransaction")
                .tasklet(cancelTransactionTasklet())
                .build();
    }

    @Bean
    public Step sendNotification(){
        return stepBuilderFactory.get("sendNotification")
                .tasklet(sendNotificationTasklet())
                .build();
    }

    @Bean
    public Job transactionPaymentsJob(){
        return jobBuilderFactory.get("transactionPaymentsJob")
                .start(validateAccount())
                    .on("VALID").to(processPayment())
                    .next(sendNotification())

                .from(validateAccount())
                    .on("INVALID").to(cancelTransaction())
                    .next(sendNotification())

                .end()
                .build();
    }
}