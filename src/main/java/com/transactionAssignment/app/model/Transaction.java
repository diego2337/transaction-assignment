package com.transactionAssignment.app.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "transaction")
@RequiredArgsConstructor
public class Transaction {
    public Transaction(
            UUID uuid,
            String accountId,
            UUID categoryId,
            UUID merchantId,
            String mcc,
            float amount,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = uuid;
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.merchantId = merchantId;
        this.mcc = mcc;
        this.totalAmount = amount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Id
    private UUID id;

    @NotNull
    private String accountId;

    @NotNull
    private UUID categoryId;

    @NotNull
    private UUID merchantId;

    @NotNull
    private String mcc;

    @NotNull
    private float totalAmount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accountId", referencedColumnName = "id", insertable = false, updatable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoryId", referencedColumnName = "id", insertable = false, updatable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "merchantId", referencedColumnName = "id", insertable = false, updatable = false)
    private Merchant merchant;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
