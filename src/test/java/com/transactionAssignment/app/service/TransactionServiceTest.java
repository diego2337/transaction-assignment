package com.transactionAssignment.app.service;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.transactionAssignment.app.dto.TransactionRequestDTO;
import com.transactionAssignment.app.dto.TransactionResponseDTO;
import org.eclipse.jetty.http.HttpStatus;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class TransactionServiceTest {
    @Mock
    TransactionService transactionService;

    @Mock
    AccountRepository accountRepository;

    @BeforeAll
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("com.transactionAssignment.app.fixtures");
    }

    @Test
    public void testExecuteFoodTransaction() {
        TransactionRequestDTO transactionRequestDTO = Fixture.from(TransactionRequestDTO.class).gimme("valid-food");
        when(accountRepository.findById).thenReturn(Fixture.from(Account.class).gimme("account"));
        when(accountRepository.findCategory).thenReturn(AccountCategory.class).gimme("account-category");
        when(transactionService.hasEnoughCredit).thenReturn(true);

        TransactionResponseDTO transactionResponseDTO = transactionService.execute(transactionRequestDTO);

        assertEquals(transactionResponseDTO.getCode(), "00");
    }
}
