package com.transactionAssignment.app.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "category")
public class Category {
    @Id
    private UUID id;

    @NotNull
    private String name;

    @OneToMany(mappedBy = "category")
    private List<AccountCategory> accountCategories;

    @OneToMany(mappedBy = "categoryId")
    private List<MccCategory> mccCategories;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
