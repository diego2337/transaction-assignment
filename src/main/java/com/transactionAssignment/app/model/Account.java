package com.transactionAssignment.app.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

import java.util.Optional;

@Getter
@Setter
@Entity
@Table(name = "account")
@Slf4j
public class Account {
    @Id
    private String id;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountCategory> accountCategories;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Optional<AccountCategory> findCategoryByMccs(List<String> mccs) {
        return accountCategories.stream()
                .filter(accountCategory -> accountCategory.getCategory().getMccCategories().stream()
                        .anyMatch(mccCategory -> mccs.contains(mccCategory.getMcc())))
                .findFirst();
    }
}
