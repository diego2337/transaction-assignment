package com.transactionAssignment.app.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class TransactionRequestDTO {
    @NotBlank(message = "accountId must not be empty")
    private String accountId;

    @NotNull(message = "totalAmount must be provided")
    @Positive(message = "totalAmount must be a positive number")
    private float totalAmount;

    @NotBlank(message = "merchant must not be empty")
    private String merchant;

    @NotBlank(message = "mcc must not be empty")
    private String mcc;
}
