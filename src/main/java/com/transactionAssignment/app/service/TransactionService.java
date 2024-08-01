package com.transactionAssignment.app.service;

import com.transactionAssignment.app.dto.TransactionRequestDTO;
import com.transactionAssignment.app.dto.TransactionResponseDTO;
import com.transactionAssignment.app.model.Account;
import com.transactionAssignment.app.model.AccountCategory;
import com.transactionAssignment.app.model.Merchant;
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
import java.util.Collections;
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
    private MerchantService merchantService;

    public static final String CASH = "5011";

    @Transactional(rollbackFor = Exception.class)
    public TransactionResponseDTO authorize(TransactionRequestDTO transactionRequestDTO) {
        try {
            log.info("Transaction::authorize - try to find account by id -> {}", transactionRequestDTO.getAccountId());
            Optional<Account> accountOptional = accountRepository.findById(transactionRequestDTO.getAccountId());
            if (!accountOptional.isPresent()) {
                log.warn("Transaction::authorize - account not found");
                return new TransactionResponseDTO("07");
            }

            Account account = accountOptional.get();
            String merchantName = formatMerchantName(transactionRequestDTO.getMerchant());

            log.info("Transaction::authorize - try to find by merchant name -> {}", transactionRequestDTO.getMerchant());
            Optional<Merchant> merchantOptional = merchantService.findByName(merchantName);
            Optional<AccountCategory> accountCategoryOptional;
            Merchant merchant;
            if (merchantOptional.isPresent()) {
            merchant = merchantOptional.get();
                log.info("Transaction::authorize - try to find accountCategory (merchant), mcc -> {}", merchant.getMccsFromCategory());
                accountCategoryOptional = account.findCategoryByMccs(merchant.getMccsFromCategory());
                if (accountCategoryOptional.isPresent() && accountCategoryOptional.get().hasEnoughCredit(transactionRequestDTO.getTotalAmount())) {
                    log.info("Transaction::authorize - set mcc from merchant");
                    transactionRequestDTO.setMcc(merchant.getMccsFromCategory().stream().findFirst().orElse(CASH));
                    merchant = upsertMerchant(merchantName, accountCategoryOptional.get().getCategory().getId());
                    return processTransaction(transactionRequestDTO, account, accountCategoryOptional.get(), merchant);
                }
            }

            accountCategoryOptional = account.findCategoryByMccs(Collections.singletonList(transactionRequestDTO.getMcc()));
            if (accountCategoryOptional.isPresent() && accountCategoryOptional.get().hasEnoughCredit(transactionRequestDTO.getTotalAmount())) {
                merchant = upsertMerchant(merchantName, accountCategoryOptional.get().getCategory().getId());
                return processTransaction(transactionRequestDTO, account, accountCategoryOptional.get(), merchant);
            }

            log.info("Transaction::authorize - account does not have mcc; try to authorize with fallback using CASH category");
            transactionRequestDTO.setMcc(CASH);
            accountCategoryOptional = account.findCategoryByMccs(Collections.singletonList(CASH));
            if (accountCategoryOptional.isPresent() && accountCategoryOptional.get().hasEnoughCredit(transactionRequestDTO.getTotalAmount())) {
                merchant = upsertMerchant(merchantName, accountCategoryOptional.get().getCategory().getId());
                return processTransaction(transactionRequestDTO, account, accountCategoryOptional.get(), merchant);
            }
            log.info("Transaction::authorize - no cash found for any category, return status");
            return new TransactionResponseDTO("51"); // No sufficient credit

        } catch (Exception e) {
            log.error("Transaction::authorize - ERROR: {}", e.getMessage());
            return new TransactionResponseDTO("07");
        }
    }

    private String formatMerchantName(String merchantName) {
        return merchantName.trim().replaceAll("\\s+", " ").toLowerCase();
    }

    public Merchant upsertMerchant(String merchantName, UUID categoryId) {
        log.info("Transaction::upsertMerchant - upsert merchant information -> {} for category -> {}", merchantName, categoryId);
        return merchantService.upsertMerchant(merchantName, categoryId);
    }

    @Transactional(rollbackFor = Exception.class)
    public TransactionResponseDTO processTransaction(
            TransactionRequestDTO transactionRequestDTO,
            Account account,
            AccountCategory accountCategory,
            Merchant merchant
    ) {
        log.info("Transaction::updateAccountAndAuthorizeTransaction - subtract value and update timestamp from account");
        accountCategory.setTotalAmount(accountCategory.getTotalAmount() - transactionRequestDTO.getTotalAmount());
        account.setUpdatedAt(LocalDateTime.now());
        log.info("Transaction::updateAccountAndAuthorizeTransaction - save account changes in database");
        accountRepository.save(account);

        log.info("Transaction::updateAccountAndAuthorizeTransaction - create transaction");
        Transaction transaction = new Transaction(
                UUID.randomUUID(),
                transactionRequestDTO.getAccountId(),
                accountCategory.getCategory().getId(),
                merchant.getId(),
                transactionRequestDTO.getMcc(),
                transactionRequestDTO.getTotalAmount(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        log.info("Transaction::updateAccountAndAuthorizeTransaction - save transaction");
        transactionRepository.save(transaction);
        log.info("Transaction::updateAccountAndAuthorizeTransaction - return status OK");
        return new TransactionResponseDTO("00"); // Transaction successful
    }
}
