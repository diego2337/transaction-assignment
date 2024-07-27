package com.transactionAssignment.app.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "employer_delivery_agent")
@IdClass(AccountCategory.class)
public class AccountCategory {
    @Id
    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @Id
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    private float totalAmount;

    public boolean hasEnoughCredit(float amount) {
        return totalAmount - amount > 0;
    }
}
