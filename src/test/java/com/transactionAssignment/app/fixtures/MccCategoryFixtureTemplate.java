package com.transactionAssignment.app.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.transactionAssignment.app.model.MccCategory;

import java.time.LocalDateTime;
import java.util.UUID;

public class MccCategoryFixtureTemplate implements TemplateLoader {
    @Override
    public void load() {
        Fixture.of(MccCategory.class).addTemplate("mcc-category-food", new Rule(){{
            add("mcc", "5411");
            add("categoryId", UUID.randomUUID());
            add("createdAt", LocalDateTime.now());
            add("updatedAt", LocalDateTime.now());
        }});

        Fixture.of(MccCategory.class).addTemplate("mcc-category-meal", new Rule(){{
            add("mcc", "5811");
            add("categoryId", UUID.randomUUID());
            add("createdAt", LocalDateTime.now());
            add("updatedAt", LocalDateTime.now());
        }});

        Fixture.of(MccCategory.class).addTemplate("mcc-category-cash", new Rule(){{
            add("mcc", "5011");
            add("categoryId", UUID.randomUUID());
            add("createdAt", LocalDateTime.now());
            add("updatedAt", LocalDateTime.now());
        }});
    }
}
