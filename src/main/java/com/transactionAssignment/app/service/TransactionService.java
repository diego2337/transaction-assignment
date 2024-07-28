package com.transactionAssignment.app.service;

import com.transactionAssignment.app.dto.TransactionRequestDTO;
import com.transactionAssignment.app.dto.TransactionResponseDTO;
import com.transactionAssignment.app.model.Account;
import com.transactionAssignment.app.model.AccountCategory;
import com.transactionAssignment.app.model.Transaction;
import com.transactionAssignment.app.repository.AccountRepository;
import com.transactionAssignment.app.repository.CategoryRepository;
import com.transactionAssignment.app.repository.TransactionRepository;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Setter
public class TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(rollbackFor = Exception.class)
    public TransactionResponseDTO authorize(TransactionRequestDTO transactionRequestDTO) {
        try {
            log.info("Transaction::execute - try to find mcc -> {}", transactionRequestDTO.getMcc());
            if (categoryRepository.findById(transactionRequestDTO.getMcc()).isPresent()) {
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
                            log.info("Transaction::execute - save account changes in database");
                            this.accountRepository.save(accountToModify);

                            log.info("Transaction::execute - create transaction and save in database");
                            Transaction transaction = new Transaction(
                                    UUID.randomUUID(),
                                    transactionRequestDTO.getAccountId(),
                                    transactionRequestDTO.getMcc(),
                                    transactionRequestDTO.getMerchant(),
                                    transactionRequestDTO.getAmount(),
                                    LocalDateTime.now(),
                                    LocalDateTime.now());
                            this.transactionRepository.save(transaction);
                            log.info("Transaction::execute - return status OK transaction");
                            return new TransactionResponseDTO("00");
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Transaction::execute - ERROR: error -> {}", e.getMessage());
        }
        return new TransactionResponseDTO("07");
    }
}
