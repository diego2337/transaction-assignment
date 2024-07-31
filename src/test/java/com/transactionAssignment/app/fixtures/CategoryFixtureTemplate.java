package com.transactionAssignment.app.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.transactionAssignment.app.enums.CategoryStatusEnum;
import com.transactionAssignment.app.model.Category;

import java.time.LocalDateTime;

public class CategoryFixtureTemplate implements TemplateLoader {
    @Override
    public void load() {
        Fixture.of(Category.class).addTemplate("category-food", new Rule(){{
            add("mcc", "5411");
            add("category", CategoryStatusEnum.FOOD);
            add("createdAt", LocalDateTime.now());
            add("updatedAt", LocalDateTime.now());
        }});

        Fixture.of(Category.class).addTemplate("category-meal", new Rule(){{
            add("mcc", "5811");
            add("category", CategoryStatusEnum.MEAL);
            add("createdAt", LocalDateTime.now());
            add("updatedAt", LocalDateTime.now());
        }});

        Fixture.of(Category.class).addTemplate("category-cash", new Rule(){{
            add("mcc", "5011");
            add("category", CategoryStatusEnum.CASH);
            add("createdAt", LocalDateTime.now());
            add("updatedAt", LocalDateTime.now());
        }});
    }
}
