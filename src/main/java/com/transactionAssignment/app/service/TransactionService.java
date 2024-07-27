package com.transactionAssignment.app.service;

import com.transactionAssignment.app.dto.TransactionRequestDTO;
import com.transactionAssignment.app.dto.TransactionResponseDTO;
import com.transactionAssignment.app.model.Account;
import com.transactionAssignment.app.model.AccountCategory;
import com.transactionAssignment.app.repository.AccountRepository;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@Setter
public class TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    public TransactionResponseDTO execute(TransactionRequestDTO transactionRequestDTO) {
        try {
            log.info("Transaction::execute - try to find account by id -> {}", transactionRequestDTO.getAccountId());
            Optional<Account> account = this.accountRepository.findById(transactionRequestDTO.getId());
            if (account.isPresent()) {
                Account accountToModify = account.get();
                log.info("Transaction::execute - account found, id -> {}", accountToModify.getId());
                log.info("Transaction::execute - try to find accountCategory, mcc -> {}", transactionRequestDTO.getMcc());
                Optional<AccountCategory> accountCategory = accountToModify.findCategoryByMcc(transactionRequestDTO.getMcc());
                if (accountCategory.isPresent()) {
                    AccountCategory accountCategoryToModify = accountCategory.get();
                    log.info("Transaction::execute - accountCategory found");
                    if (accountCategory.get().hasEnoughCredit(transactionRequestDTO.getAmount())) {
                        log.info("Transaction::execute - has enough amount; subtract value and update timestamp");
                        accountCategoryToModify.setTotalAmount(accountCategoryToModify.getTotalAmount() - transactionRequestDTO.getAmount());
                        accountToModify.setUpdatedAt(LocalDateTime.now());
                        log.info("Transaction::execute - save changes in database");
                        this.accountRepository.save(accountToModify);

                        return new TransactionResponseDTO("00");
                    }
                }
            }
        } catch (Exception e) {
            log.error("Transaction::execute - ERROR: error -> {}", e.getMessage());
        }
        return new TransactionResponseDTO("07");
    }
}
