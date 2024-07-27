package com.transactionAssignment.app.dto;

import lombok.Data;

@Data
public class TransactionRequestDTO {
    private String id;
    private String accountId;
    private float amount;
    private String merchant;
    private String mcc;
}
