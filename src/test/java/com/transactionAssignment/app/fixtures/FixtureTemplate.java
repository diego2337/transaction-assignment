package com.transactionAssignment.app.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.transactionAssignment.app.dto.TransactionRequestDTO;
import com.transactionAssignment.app.enums.CategoryStatusEnum;
import com.transactionAssignment.app.model.Account;
import com.transactionAssignment.app.model.AccountCategory;
import com.transactionAssignment.app.model.Category;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

public class FixtureTemplate implements TemplateLoader {
    @Override
    public void load() {
        Fixture.of(TransactionRequestDTO.class).addTemplate("valid-food", new Rule(){{
            add("id", UUID.randomUUID().toString());
            add("accountId", UUID.randomUUID().toString());
            add("amount", 23.56f);
            add("merchant", "Food");
            add("mcc", "5411");
        }});

        Fixture.of(TransactionRequestDTO.class).addTemplate("valid-meal", new Rule(){{
            add("id", UUID.randomUUID().toString());
            add("accountId", UUID.randomUUID().toString());
            add("amount", "234.56");
            add("merchant", "Meal");
            add("mcc", "5811");
        }});

        Fixture.of(AccountCategory.class).addTemplate("account-category-food", new Rule(){{
//            add("account", one(Account.class, "account"));
            add("category", one(Category.class, "category-food"));
            add("totalAmount", 100.00f);
        }});

        Fixture.of(AccountCategory.class).addTemplate("account-category-meal", new Rule(){{
//            add("account", one(Account.class, "account"));
            add("category", one(Category.class, "category-meal"));
            add("totalAmount", 100.00f);
        }});

        Fixture.of(Account.class).addTemplate("account", new Rule(){{
            add("id", UUID.randomUUID().toString());
            add("accountCategories", has(3).of(AccountCategory.class, "account-category-food", "account-category-meal", "account-category-meal"));
            add("createdAt", LocalDateTime.now());
            add("updatedAt", LocalDateTime.now());
        }});

        Fixture.of(Category.class).addTemplate("category-food", new Rule(){{
            add("id", UUID.randomUUID().toString());
            add("mcc", "5411");
            add("category", CategoryStatusEnum.FOOD);
            add("createdAt", LocalDateTime.now());
            add("updatedAt", LocalDateTime.now());
        }});

        Fixture.of(Category.class).addTemplate("category-meal", new Rule(){{
            add("id", UUID.randomUUID().toString());
            add("mcc", "5811");
            add("category", CategoryStatusEnum.MEAL);
            add("createdAt", LocalDateTime.now());
            add("updatedAt", LocalDateTime.now());
        }});
    }
}
