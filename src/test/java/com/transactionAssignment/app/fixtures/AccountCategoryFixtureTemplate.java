package com.transactionAssignment.app.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.transactionAssignment.app.model.AccountCategory;
import com.transactionAssignment.app.model.Category;

public class AccountCategoryFixtureTemplate implements TemplateLoader {
    @Override
    public void load() {
        Fixture.of(AccountCategory.class).addTemplate("account-category-food", new Rule(){{
            add("category", one(Category.class, "category-food"));
            add("totalAmount", 100.00f);
        }});

        Fixture.of(AccountCategory.class).addTemplate("account-category-meal", new Rule(){{
            add("category", one(Category.class, "category-meal"));
            add("totalAmount", 100.00f);
        }});

        Fixture.of(AccountCategory.class).addTemplate("account-category-cash", new Rule(){{
            add("category", one(Category.class, "category-cash"));
            add("totalAmount", 100.00f);
        }});

        Fixture.of(AccountCategory.class).addTemplate("account-category-food-with-no-money", new Rule(){{
            add("category", one(Category.class, "category-food"));
            add("totalAmount", 0.0f);
        }});

        Fixture.of(AccountCategory.class).addTemplate("account-category-meal-with-no-money", new Rule(){{
            add("category", one(Category.class, "category-meal"));
            add("totalAmount", 0.0f);
        }});

        Fixture.of(AccountCategory.class).addTemplate("account-category-cash-with-no-money", new Rule(){{
            add("category", one(Category.class, "category-cash"));
            add("totalAmount", 0.0f);
        }});
    }
}
