package com.transactionAssignment.app.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

import java.util.Optional;

@Getter
@Setter
@Entity
@Table(name = "account")
public class Account {
    @Id
    private String id;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountCategory> accountCategories;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Optional<AccountCategory> findCategoryByMcc(String mcc) {
        return accountCategories.stream().filter(accountCategory -> accountCategory.getCategory().getMcc().equals(mcc)).findFirst();
    }
}
