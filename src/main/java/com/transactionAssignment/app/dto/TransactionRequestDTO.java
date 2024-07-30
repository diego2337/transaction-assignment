package com.transactionAssignment.app.dto;

import lombok.Data;

@Data
public class TransactionRequestDTO {
    private String accountId;
    private float totalAmount;
    private String merchant;
    private String mcc;
}
