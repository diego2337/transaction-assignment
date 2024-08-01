package com.transactionAssignment.app.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "mcc_category")
public class MccCategory {

    @Id
    private String mcc;

    @NotNull
    private UUID categoryId;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
