package com.transactionAssignment.app.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.transactionAssignment.app.model.Account;
import com.transactionAssignment.app.model.AccountCategory;

import java.time.LocalDateTime;
import java.util.UUID;

public class AccountFixtureTemplate implements TemplateLoader {
    @Override
    public void load() {
        Fixture.of(Account.class).addTemplate("account", new Rule(){{
            add("id", UUID.randomUUID().toString());
            add("accountCategories", has(3).of(AccountCategory.class, "account-category-food", "account-category-meal", "account-category-cash"));
            add("createdAt", LocalDateTime.now());
            add("updatedAt", LocalDateTime.now());
        }});

        Fixture.of(Account.class).addTemplate("account-with-no-money", new Rule(){{
            add("id", UUID.randomUUID().toString());
            add("accountCategories", has(3).of(
                    AccountCategory.class, "account-category-food-with-no-money", "account-category-meal-with-no-money", "account-category-cash-with-no-money"
            ));
            add("createdAt", LocalDateTime.now());
            add("updatedAt", LocalDateTime.now());
        }});

        Fixture.of(Account.class).addTemplate("account-food-only", new Rule(){{
            add("id", UUID.randomUUID().toString());
            add("accountCategories", has(2).of(AccountCategory.class, "account-category-food", "account-category-food"));
            add("createdAt", LocalDateTime.now());
            add("updatedAt", LocalDateTime.now());
        }});

        Fixture.of(Account.class).addTemplate("account-food-and-cash", new Rule(){{
            add("id", UUID.randomUUID().toString());
            add("accountCategories", has(2).of(AccountCategory.class, "account-category-food", "account-category-cash"));
            add("createdAt", LocalDateTime.now());
            add("updatedAt", LocalDateTime.now());
        }});
    }
}
