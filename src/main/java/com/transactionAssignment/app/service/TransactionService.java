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

    public static String CASH = "5011";

    @Transactional(rollbackFor = Exception.class)
    public TransactionResponseDTO authorize(TransactionRequestDTO transactionRequestDTO) {
        try {
            log.info("Transaction::authorize - try to find by merchant name -> {}", transactionRequestDTO.getMerchant());

            log.info("Transaction::authorize - try to find account by id -> {}", transactionRequestDTO.getAccountId());
            Optional<Account> account = this.accountRepository.findById(transactionRequestDTO.getAccountId());
            Optional<AccountCategory> accountCategory;
            if (account.isPresent()) {
                Account accountToModify = account.get();
                log.info("Transaction::authorize - try to find mcc -> {}", transactionRequestDTO.getMcc());
                if (categoryRepository.findById(transactionRequestDTO.getMcc()).isPresent()) {
                    log.info("Transaction::authorize - account found, id -> {}", accountToModify.getId());
                    log.info("Transaction::authorize - try to find accountCategory, mcc -> {}", transactionRequestDTO.getMcc());
                    accountCategory = accountToModify.findCategoryByMcc(transactionRequestDTO.getMcc());
                    if (accountCategory.isPresent() && accountCategory.get().hasEnoughCredit(transactionRequestDTO.getTotalAmount())) {
                        log.info("Transaction::authorize - accountCategory found for mcc -> {}", transactionRequestDTO.getMcc());
                        return this.updateAccountAndAuthorizeTransaction(transactionRequestDTO, accountToModify, accountCategory.get());
                    }
                }
                log.info("Transaction::authorize - account does not have mcc; try to authorize with fallback using CASH category");
                transactionRequestDTO.setMcc(CASH);
                accountCategory = accountToModify.findCategoryByMcc(transactionRequestDTO.getMcc());
                if (accountCategory.isPresent() && accountCategory.get().hasEnoughCredit(transactionRequestDTO.getTotalAmount())) {
                    log.info("Transaction::authorize - accountCategory found for CASH -> {}", CASH);
                    return this.updateAccountAndAuthorizeTransaction(transactionRequestDTO, accountToModify, accountCategory.get());
                }
                log.info("Transaction::authorize - no cash found for any category, return status");
                return new TransactionResponseDTO("51");
            }
        } catch (Exception e) {
            log.error("Transaction::authorize - ERROR: error -> {}", e.getMessage());
        }
        return new TransactionResponseDTO("07");
    }

    @Transactional(rollbackFor = Exception.class)
    public TransactionResponseDTO updateAccountAndAuthorizeTransaction(
            TransactionRequestDTO transactionRequestDTO,
            Account account,
            AccountCategory accountCategory
    ) {
        log.info("Transaction::updateAccountAndAuthorizeTransaction - has enough amount; subtract value and update timestamp");
        accountCategory.setTotalAmount(accountCategory.getTotalAmount() - transactionRequestDTO.getTotalAmount());
        account.setUpdatedAt(LocalDateTime.now());
        log.info("Transaction::updateAccountAndAuthorizeTransaction - save account changes in database");
        this.accountRepository.save(account);

        log.info("Transaction::updateAccountAndAuthorizeTransaction - create transaction and save in database");
        Transaction transaction = new Transaction(
                UUID.randomUUID(),
                transactionRequestDTO.getAccountId(),
                transactionRequestDTO.getMcc(),
                transactionRequestDTO.getMerchant(),
                transactionRequestDTO.getTotalAmount(),
                LocalDateTime.now(),
                LocalDateTime.now());
        this.transactionRepository.save(transaction);
        log.info("Transaction::updateAccountAndAuthorizeTransaction - return status OK transaction");
        return new TransactionResponseDTO("00");
    }
}
