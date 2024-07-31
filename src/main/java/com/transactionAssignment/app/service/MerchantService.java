package com.transactionAssignment.app.service;

import com.transactionAssignment.app.model.Merchant;
import com.transactionAssignment.app.repository.MerchantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class MerchantService {

    @Autowired
    MerchantRepository merchantRepository;

    public Optional<Merchant> findByName(String name) {
        return this.merchantRepository.findByName(name);
    }

    public void upsertMerchant(String name, String mcc) {
        Optional<Merchant> existingMerchant = this.merchantRepository.findByName(name);
        Merchant upsertMerchant;
        if (existingMerchant.isPresent()) {
            log.info("MerchantService::upsertMerchant - merchant exists in database; update mcc and updated_at timestamp");
            upsertMerchant = existingMerchant.get();
            upsertMerchant.setMcc(mcc);
            upsertMerchant.setUpdatedAt(LocalDateTime.now());
        } else {
            log.info("MerchantService::upsertMerchant - merchant does not exist in database; create new");
            upsertMerchant = new Merchant(
                    UUID.randomUUID(),
                    name,
                    mcc,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
        }
        log.info("MerchantService::upsertMerchant - save merchant");
        merchantRepository.save(upsertMerchant);
    }
}
