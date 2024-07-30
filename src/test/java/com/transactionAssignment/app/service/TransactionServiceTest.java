package com.transactionAssignment.app.service;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.transactionAssignment.app.dto.TransactionRequestDTO;
import com.transactionAssignment.app.dto.TransactionResponseDTO;
import com.transactionAssignment.app.model.Account;
import com.transactionAssignment.app.model.AccountCategory;
import com.transactionAssignment.app.model.Category;
import com.transactionAssignment.app.repository.AccountRepository;
import com.transactionAssignment.app.repository.CategoryRepository;
import com.transactionAssignment.app.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @InjectMocks
    @Spy
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
    public void testAuthorizeFoodTransaction() {
        TransactionRequestDTO transactionRequestDTO = Fixture.from(TransactionRequestDTO.class).gimme("valid-food");
        Account expectedAccount = Fixture.from(Account.class).gimme("account");
        doReturn(Optional.of(Category.class)).when(this.categoryRepository).findById(transactionRequestDTO.getMcc());
        doReturn(Optional.of(expectedAccount)).when(this.accountRepository).findById(transactionRequestDTO.getAccountId());

        TransactionResponseDTO transactionResponseDTO = transactionService.authorize(transactionRequestDTO);

        assertEquals(transactionResponseDTO.getCode(), "00");
        verify(this.categoryRepository, times(1)).findById(transactionRequestDTO.getMcc());
        verify(this.accountRepository, times(1)).findById(transactionRequestDTO.getAccountId());
        verify(this.transactionService, times(1)).updateAccountAndAuthorizeTransaction(
                any(),
                any(),
                any()
        );
    }

    @Test
    public void testAuthorizeAccountNotFound() {
        TransactionRequestDTO transactionRequestDTO = Fixture.from(TransactionRequestDTO.class).gimme("valid-food");
        doReturn(Optional.of(Category.class)).when(this.categoryRepository).findById(transactionRequestDTO.getMcc());
        doReturn(Optional.empty()).when(this.accountRepository).findById(transactionRequestDTO.getAccountId());

        TransactionResponseDTO transactionResponseDTO = transactionService.authorize(transactionRequestDTO);

        assertEquals(transactionResponseDTO.getCode(), "07");
        verify(this.categoryRepository, times(1)).findById(transactionRequestDTO.getMcc());
        verify(this.accountRepository, times(1)).findById(transactionRequestDTO.getAccountId());
        verify(this.transactionService, never()).updateAccountAndAuthorizeTransaction(
                any(),
                any(),
                any()
        );
    }

    @Test
    public void testAuthorizeNotFindAccountWithMealCategoryButFindWithCashCategory() {
        TransactionRequestDTO transactionRequestDTO = Fixture.from(TransactionRequestDTO.class).gimme("valid-meal");
        Account expectedAccount = Fixture.from(Account.class).gimme("account-food-and-cash");
        doReturn(Optional.of(Category.class)).when(this.categoryRepository).findById(transactionRequestDTO.getMcc());
        doReturn(Optional.of(expectedAccount)).when(this.accountRepository).findById(transactionRequestDTO.getAccountId());

        TransactionResponseDTO transactionResponseDTO = transactionService.authorize(transactionRequestDTO);

        assertEquals(transactionResponseDTO.getCode(), "00");
        verify(this.categoryRepository, times(1)).findById(transactionRequestDTO.getMcc());
        verify(this.accountRepository, times(1)).findById(transactionRequestDTO.getAccountId());
        verify(this.transactionService, times(1)).updateAccountAndAuthorizeTransaction(
                any(),
                any(),
                any()
        );
    }

    @Test
    public void testAuthorizeHasNotEnoughCreditForAnyCategory() {
        TransactionRequestDTO transactionRequestDTO = Fixture.from(TransactionRequestDTO.class).gimme("valid-food");
        Account expectedAccount = Fixture.from(Account.class).gimme("account-with-no-money");
        doReturn(Optional.of(Category.class)).when(this.categoryRepository).findById(transactionRequestDTO.getMcc());
        doReturn(Optional.of(expectedAccount)).when(this.accountRepository).findById(transactionRequestDTO.getAccountId());

        TransactionResponseDTO transactionResponseDTO = transactionService.authorize(transactionRequestDTO);

        assertEquals(transactionResponseDTO.getCode(), "51");
        verify(this.categoryRepository, times(1)).findById(transactionRequestDTO.getMcc());
        verify(this.accountRepository, times(1)).findById(transactionRequestDTO.getAccountId());
        verify(this.transactionService, never()).updateAccountAndAuthorizeTransaction(
                any(),
                any(),
                any()
        );
    }

    @Test
    public void testAuthorizeFindNoMcc() {
        TransactionRequestDTO transactionRequestDTO = Fixture.from(TransactionRequestDTO.class).gimme("invalid-mcc");
        doReturn(Optional.empty()).when(this.categoryRepository).findById(transactionRequestDTO.getMcc());

        TransactionResponseDTO transactionResponseDTO = transactionService.authorize(transactionRequestDTO);

        assertEquals(transactionResponseDTO.getCode(), "07");
        verify(this.accountRepository, never()).findById(any());
        verify(this.accountRepository, never()).save(any());
        verify(this.transactionRepository, never()).save(any());
    }

    @Test
    public void testUpdateAccountAndAuthorizeTransaction() {
        TransactionRequestDTO transactionRequestDTO = Fixture.from(TransactionRequestDTO.class).gimme("valid-food");
        Account account = Fixture.from(Account.class).gimme("account");
        AccountCategory accountCategory = Fixture.from(AccountCategory.class).gimme("account-category-food");
        Account accountSpy = Mockito.spy(account);
        AccountCategory accountCategorySpy = Mockito.spy(accountCategory);

        TransactionResponseDTO transactionResponseDTO =
                transactionService.updateAccountAndAuthorizeTransaction(transactionRequestDTO, accountSpy, accountCategorySpy);

        assertEquals(transactionResponseDTO.getCode(), "00");
        verify(accountCategorySpy, times(1)).setTotalAmount(
                accountCategory.getTotalAmount() - transactionRequestDTO.getTotalAmount()
        );
        verify(accountSpy, times(1)).setUpdatedAt(any());
        verify(this.accountRepository, times(1)).save(any());
        verify(this.transactionRepository, times(1)).save(any());
    }
}
