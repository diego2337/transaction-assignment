package com.transactionAssignment.app.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class AccountCategoryId implements Serializable {
    private String account;
    private String category;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((account == null) ? 0 : account.hashCode());
        result = prime * result
                + ((category == null) ? 0 : category.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AccountCategoryId other = (AccountCategoryId) obj;
        return Objects.equals(getAccount(), other.getAccount()) && Objects.equals(getCategory(), other.getCategory());
    }
}
