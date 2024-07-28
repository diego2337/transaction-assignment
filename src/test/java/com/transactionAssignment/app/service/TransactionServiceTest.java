package com.transactionAssignment.app.service;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.transactionAssignment.app.dto.TransactionRequestDTO;
import com.transactionAssignment.app.dto.TransactionResponseDTO;
import com.transactionAssignment.app.model.Account;
import com.transactionAssignment.app.model.Category;
import com.transactionAssignment.app.repository.AccountRepository;
import com.transactionAssignment.app.repository.CategoryRepository;
import com.transactionAssignment.app.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @InjectMocks
    TransactionService transactionService;

    @Mock
    AccountRepository accountRepository;

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    CategoryRepository categoryRepository;

    @BeforeAll
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("com.transactionAssignment.app.fixtures");
    }

    @Test
    public void testExecuteFoodTransaction() {
        TransactionRequestDTO transactionRequestDTO = Fixture.from(TransactionRequestDTO.class).gimme("valid-food");
        Account expectedAccount = Fixture.from(Account.class).gimme("account");
        doReturn(Optional.of(Category.class)).when(this.categoryRepository).findById(anyString());
        doReturn(Optional.of(expectedAccount)).when(this.accountRepository).findById(anyString());

        TransactionResponseDTO transactionResponseDTO = transactionService.execute(transactionRequestDTO);

        assertEquals(transactionResponseDTO.getCode(), "00");
        verify(this.accountRepository, times(1)).save(any());
        verify(this.transactionRepository, times(1)).save(any());
    }

    @Test
    public void testExecuteAccountNotFound() {
        TransactionRequestDTO transactionRequestDTO = Fixture.from(TransactionRequestDTO.class).gimme("valid-food");
        doReturn(Optional.of(Category.class)).when(this.categoryRepository).findById(anyString());
        doReturn(Optional.empty()).when(this.accountRepository).findById(anyString());

        TransactionResponseDTO transactionResponseDTO = transactionService.execute(transactionRequestDTO);

        assertEquals(transactionResponseDTO.getCode(), "07");
        verify(this.accountRepository, never()).save(any());
        verify(this.transactionRepository, never()).save(any());
    }

    @Test
    public void testExecuteFindAccountButNotAccountCategory() {
        TransactionRequestDTO transactionRequestDTO = Fixture.from(TransactionRequestDTO.class).gimme("valid-meal");
        Account expectedAccount = Fixture.from(Account.class).gimme("account-food-only");
        doReturn(Optional.of(Category.class)).when(this.categoryRepository).findById(anyString());
        doReturn(Optional.of(expectedAccount)).when(this.accountRepository).findById(anyString());

        TransactionResponseDTO transactionResponseDTO = transactionService.execute(transactionRequestDTO);

        assertEquals(transactionResponseDTO.getCode(), "07");
        verify(this.accountRepository, never()).save(any());
        verify(this.transactionRepository, never()).save(any());
    }

    @Test
    public void testExecuteFindAccountCategoryButHasNotEnoughCredit() {
        TransactionRequestDTO transactionRequestDTO = Fixture.from(TransactionRequestDTO.class).gimme("valid-food");
        Account expectedAccount = Fixture.from(Account.class).gimme("account-with-no-money");
        doReturn(Optional.of(Category.class)).when(this.categoryRepository).findById(anyString());
        doReturn(Optional.of(expectedAccount)).when(this.accountRepository).findById(anyString());

        TransactionResponseDTO transactionResponseDTO = transactionService.execute(transactionRequestDTO);

        assertEquals(transactionResponseDTO.getCode(), "07");
        verify(this.accountRepository, never()).save(any());
        verify(this.transactionRepository, never()).save(any());
    }

    @Test
    public void testExecuteFindNoMcc() {
        TransactionRequestDTO transactionRequestDTO = Fixture.from(TransactionRequestDTO.class).gimme("invalid-mcc");
        doReturn(Optional.empty()).when(this.categoryRepository).findById(anyString());

        TransactionResponseDTO transactionResponseDTO = transactionService.execute(transactionRequestDTO);

        assertEquals(transactionResponseDTO.getCode(), "07");
        verify(this.accountRepository, never()).findById(any());
        verify(this.accountRepository, never()).save(any());
        verify(this.transactionRepository, never()).save(any());
    }
}
