package com.transactionAssignment.app.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.transactionAssignment.app.model.Category;
import com.transactionAssignment.app.model.MccCategory;

import java.time.LocalDateTime;
import java.util.UUID;

public class CategoryFixtureTemplate implements TemplateLoader {
    @Override
    public void load() {
        Fixture.of(Category.class).addTemplate("category-food", new Rule(){{
            add("id", UUID.randomUUID());
            add("name", "FOOD");
            add("mccCategories", has(2).of(MccCategory.class, "mcc-category-food"));
            add("createdAt", LocalDateTime.now());
            add("updatedAt", LocalDateTime.now());
        }});

        Fixture.of(Category.class).addTemplate("category-meal", new Rule(){{
            add("id", UUID.randomUUID());
            add("name", "MEAL");
            add("mccCategories", has(2).of(MccCategory.class, "mcc-category-meal"));
            add("createdAt", LocalDateTime.now());
            add("updatedAt", LocalDateTime.now());
        }});

        Fixture.of(Category.class).addTemplate("category-cash", new Rule(){{
            add("id", UUID.randomUUID());
            add("name", "CASH");
            add("mccCategories", has(1).of(MccCategory.class, "mcc-category-cash"));
            add("createdAt", LocalDateTime.now());
            add("updatedAt", LocalDateTime.now());
        }});
    }
}
