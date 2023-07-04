package com.example.springbatchflow.controllers.request;

import lombok.Data;

@Data
public class TransferPaymentDTO {
    private Double avaiableBalance;
    private Double amountPaid;
    private Boolean isEnabled;
}
