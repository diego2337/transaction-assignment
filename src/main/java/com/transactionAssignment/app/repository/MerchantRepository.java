package com.transactionAssignment.app.repository;

import com.transactionAssignment.app.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MerchantRepository extends JpaRepository<Merchant, UUID> {
    Optional<Merchant> findByName(String merchantName);
}
