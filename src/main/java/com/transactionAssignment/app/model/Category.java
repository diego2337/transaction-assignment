package com.transactionAssignment.app.model;

import com.transactionAssignment.app.enums.CategoryStatusEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Category {
    @Id
    private String mcc;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CategoryStatusEnum category;

    @OneToMany(mappedBy = "category")
    private List<AccountCategory> accountCategories;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
