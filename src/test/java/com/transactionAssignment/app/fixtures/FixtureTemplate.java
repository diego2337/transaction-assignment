package com.transactionAssignment.app.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.transactionAssignment.app.dto.TransactionRequestDTO;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class FixtureTemplate implements TemplateLoader {
    @Override
    public void load() {
        Fixture.of(TransactionRequestDTO.class).addTemplate("valid-food", new Rule(){{
            add("id", UUID.randomUUID());
            add("accountId", UUID.randomUUID());
            add("amount", "234.56");
            add("merchant", "Caju");
            add("mcc", "5411");
        }});

        Fixture.of(TransactionRequestDTO.class).addTemplate("valid-meal", new Rule(){{
            add("id", UUID.randomUUID());
            add("accountId", UUID.randomUUID());
            add("amount", "234.56");
            add("merchant", "Caju");
            add("mcc", "5811");
        }});

    }
}
