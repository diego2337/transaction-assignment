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
    public Transaction(UUID uuid, String accountId, String mcc, String merchant, float amount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = uuid;
        this.accountId = accountId;
        this.mcc = mcc;
        this.merchant = merchant;
        this.totalAmount = amount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Id
    private UUID id;

    @NotNull
    @Column(insertable = false, updatable = false)
    private String accountId;

    @NotNull
    @Column(name = "mcc", insertable = false, updatable = false)
    private String mcc;

    @NotNull
    private String merchant;

    @NotNull
    private float totalAmount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accountId", referencedColumnName = "id")
    private Account account;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mcc", referencedColumnName = "mcc")
    private Category category;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
