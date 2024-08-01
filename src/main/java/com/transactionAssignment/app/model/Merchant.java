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
@Table(name = "merchant")
@RequiredArgsConstructor
public class Merchant {
    public Merchant(UUID uuid, String name, UUID categoryId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = uuid;
        this.name = name;
        this.categoryId = categoryId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Id
    private UUID id;

    @NotNull
    private String name;

    @NotNull
    private UUID categoryId;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoryId", referencedColumnName = "id", insertable = false, updatable = false)
    private Category category;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
