package com.transactionAssignment.app.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.transactionAssignment.app.model.Category;
import com.transactionAssignment.app.model.Merchant;

import java.time.LocalDateTime;
import java.util.UUID;

public class MerchantFixtureTemplate implements TemplateLoader {
    @Override
    public void load() {
        Fixture.of(Merchant.class).addTemplate("merchant-meal", new Rule(){{
            add("id", UUID.randomUUID());
            add("name", "meal merchant");
            add("category", one(Category.class, "category-meal"));
            add("createdAt", LocalDateTime.now());
            add("updatedAt", LocalDateTime.now());
        }});

        Fixture.of(Merchant.class).addTemplate("merchant-food", new Rule(){{
            add("id", UUID.randomUUID());
            add("name", "food merchant");
            add("category", one(Category.class, "category-food"));
            add("createdAt", LocalDateTime.now());
            add("updatedAt", LocalDateTime.now());
        }});

        Fixture.of(Merchant.class).addTemplate("merchant-cash", new Rule(){{
            add("id", UUID.randomUUID());
            add("name", "cash merchant");
            add("category", one(Category.class, "category-cash"));
            add("createdAt", LocalDateTime.now());
            add("updatedAt", LocalDateTime.now());
        }});
    }
}
