package com.transactionAssignment.app.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.transactionAssignment.app.model.Merchant;

import java.time.LocalDateTime;
import java.util.UUID;

public class MerchantFixtureTemplate implements TemplateLoader {
    @Override
    public void load() {
        Fixture.of(Merchant.class).addTemplate("merchant-meal", new Rule(){{
            add("id", UUID.randomUUID());
            add("name", "meal merchant");
            add("mcc", "5811");
            add("createdAt", LocalDateTime.now());
            add("updatedAt", LocalDateTime.now());
        }});
    }
}
