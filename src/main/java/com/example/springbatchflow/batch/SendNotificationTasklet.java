package com.example.springbatchflow.batch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SendNotificationTasklet implements Tasklet{

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String transactionId = chunkContext.getStepContext().getJobParameters().get("transactionId").toString();
        log.info("notification send to client for the {} transaction",transactionId);
        return RepeatStatus.FINISHED;
    }
    
}
