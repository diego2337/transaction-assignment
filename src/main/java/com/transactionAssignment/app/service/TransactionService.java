package com.transactionAssignment.app.service;

import com.transactionAssignment.app.dto.TransactionRequestDTO;
import com.transactionAssignment.app.dto.TransactionResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;

public class TransactionService {
    @Autowired
    private AccountRepository accountRepository;
    public TransactionResponseDTO execute(TransactionRequestDTO transactionRequestDTO) {

        return new TransactionResponseDTO();
    }
}
