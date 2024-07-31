package com.transactionAssignment.app.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.transactionAssignment.app.dto.TransactionRequestDTO;

import java.util.UUID;

public class TransactionRequestDTOFixtureTemplate implements TemplateLoader {
    @Override
    public void load() {
        Fixture.of(TransactionRequestDTO.class).addTemplate("valid-food", new Rule(){{
            add("accountId", UUID.randomUUID().toString());
            add("totalAmount", 23.56f);
            add("merchant", "Food");
            add("mcc", "5411");
        }});

        Fixture.of(TransactionRequestDTO.class).addTemplate("valid-meal", new Rule(){{
            add("accountId", UUID.randomUUID().toString());
            add("totalAmount", 23.56f);
            add("merchant", "Meal");
            add("mcc", "5811");
        }});

        Fixture.of(TransactionRequestDTO.class).addTemplate("valid-merchant-invalid-mcc", new Rule(){{
            add("accountId", UUID.randomUUID().toString());
            add("totalAmount", 23.56f);
            add("merchant", "Merchant     Meal");
            add("mcc", "1111");
        }});

        Fixture.of(TransactionRequestDTO.class).addTemplate("invalid-mcc", new Rule(){{
            add("accountId", UUID.randomUUID().toString());
            add("totalAmount", 23.56f);
            add("merchant", "Unknown");
            add("mcc", "1111");
        }});

        Fixture.of(TransactionRequestDTO.class).addTemplate("valid-merchant-meal", new Rule(){{
            add("accountId", UUID.randomUUID().toString());
            add("totalAmount", 23.56f);
            add("merchant", "Meal");
            add("mcc", "5899");
        }});
    }
}
