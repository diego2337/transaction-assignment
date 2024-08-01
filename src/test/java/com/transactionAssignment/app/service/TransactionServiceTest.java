package com.transactionAssignment.app.service;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.transactionAssignment.app.dto.TransactionRequestDTO;
import com.transactionAssignment.app.dto.TransactionResponseDTO;
import com.transactionAssignment.app.model.Account;
import com.transactionAssignment.app.model.AccountCategory;
import com.transactionAssignment.app.model.Category;
import com.transactionAssignment.app.model.Merchant;
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

import static com.transactionAssignment.app.service.TransactionService.CASH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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

    @Mock
    MerchantService merchantService;

    @BeforeAll
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("com.transactionAssignment.app.fixtures");
    }

    @Test
    public void testAuthorizeFoodTransaction() {
        TransactionRequestDTO transactionRequestDTO = Fixture.from(TransactionRequestDTO.class).gimme("valid-food");
        Account expectedAccount = Fixture.from(Account.class).gimme("account");
        doReturn(Optional.of(expectedAccount)).when(accountRepository).findById(transactionRequestDTO.getAccountId());
        doReturn(Optional.of(new Category())).when(categoryRepository).findById(transactionRequestDTO.getMcc());
        doReturn(Fixture.from(Merchant.class).gimme("merchant-food")).when(transactionService).upsertMerchant(any(), any());

        TransactionResponseDTO transactionResponseDTO = transactionService.authorize(transactionRequestDTO);

        assertEquals("00", transactionResponseDTO.getCode());
        verify(accountRepository, times(1)).findById(transactionRequestDTO.getAccountId());
        verify(categoryRepository, times(1)).findById(transactionRequestDTO.getMcc());
        verify(transactionService, times(1)).processTransaction(any(), any(), any(), any());
    }

    @Test
    public void testAuthorizeAccountNotFound() {
        TransactionRequestDTO transactionRequestDTO = Fixture.from(TransactionRequestDTO.class).gimme("valid-food");
        doReturn(Optional.empty()).when(accountRepository).findById(transactionRequestDTO.getAccountId());

        TransactionResponseDTO transactionResponseDTO = transactionService.authorize(transactionRequestDTO);

        assertEquals("07", transactionResponseDTO.getCode());
        verify(accountRepository, times(1)).findById(transactionRequestDTO.getAccountId());
        verify(categoryRepository, never()).findById(transactionRequestDTO.getMcc());
        verify(transactionService, never()).processTransaction(any(), any(), any(), any());
    }

    @Test
    public void testAuthorizeNotFindAccountWithMealCategoryButFindWithCashCategory() {
        TransactionRequestDTO transactionRequestDTO = Fixture.from(TransactionRequestDTO.class).gimme("valid-meal");
        Account expectedAccount = Fixture.from(Account.class).gimme("account-food-and-cash");
        TransactionRequestDTO transactionRequestDTOSpy = Mockito.spy(transactionRequestDTO);
        doReturn(Optional.of(expectedAccount)).when(accountRepository).findById(transactionRequestDTO.getAccountId());
        doReturn(Optional.of(new Category())).when(categoryRepository).findById(transactionRequestDTO.getMcc());
        doReturn(Fixture.from(Merchant.class).gimme("merchant-cash")).when(transactionService).upsertMerchant(any(), any());

        TransactionResponseDTO transactionResponseDTO = transactionService.authorize(transactionRequestDTOSpy);

        assertEquals("00", transactionResponseDTO.getCode());
        verify(accountRepository, times(1)).findById(transactionRequestDTO.getAccountId());
        verify(categoryRepository, times(1)).findById(transactionRequestDTO.getMcc());
        verify(transactionService, times(1)).processTransaction(any(), any(), any(), any());
    }

    @Test
    public void testAuthorizeHasNotEnoughCreditForAnyCategory() {
        TransactionRequestDTO transactionRequestDTO = Fixture.from(TransactionRequestDTO.class).gimme("valid-food");
        Account expectedAccount = Fixture.from(Account.class).gimme("account-with-no-money");
        doReturn(Optional.of(expectedAccount)).when(accountRepository).findById(transactionRequestDTO.getAccountId());
        doReturn(Optional.of(new Category())).when(categoryRepository).findById(transactionRequestDTO.getMcc());

        TransactionResponseDTO transactionResponseDTO = transactionService.authorize(transactionRequestDTO);

        assertEquals("51", transactionResponseDTO.getCode());
        verify(accountRepository, times(1)).findById(transactionRequestDTO.getAccountId());
        verify(categoryRepository, times(1)).findById(any());
        verify(transactionService, never()).processTransaction(any(), any(), any(), any());
    }

    @Test
    public void testAuthorizeFindNoMccButHasCreditInCash() {
        TransactionRequestDTO transactionRequestDTO = Fixture.from(TransactionRequestDTO.class).gimme("invalid-mcc");
        Account expectedAccount = Fixture.from(Account.class).gimme("account-food-and-cash");
        TransactionRequestDTO transactionRequestDTOSpy = Mockito.spy(transactionRequestDTO);
        doReturn(Optional.of(expectedAccount)).when(accountRepository).findById(transactionRequestDTO.getAccountId());
        doReturn(Optional.empty()).when(categoryRepository).findById(transactionRequestDTO.getMcc());
        doReturn(Fixture.from(Merchant.class).gimme("merchant-cash")).when(transactionService).upsertMerchant(any(), any());

        TransactionResponseDTO transactionResponseDTO = transactionService.authorize(transactionRequestDTOSpy);

        assertEquals("00", transactionResponseDTO.getCode());
        verify(transactionRequestDTOSpy, times(1)).setMcc(CASH);
        verify(accountRepository, times(1)).findById(transactionRequestDTO.getAccountId());
        verify(categoryRepository, times(1)).findById(transactionRequestDTO.getMcc());
        verify(transactionService, times(1)).processTransaction(any(), any(), any(), any());
    }

    @Test
    public void testFindMerchantCategoryAndAuthorizeTransaction() {
        TransactionRequestDTO transactionRequestDTO = Fixture.from(TransactionRequestDTO.class).gimme("valid-merchant-invalid-mcc");
        TransactionRequestDTO transactionRequestDTOSpy = Mockito.spy(transactionRequestDTO);
        Account account = Fixture.from(Account.class).gimme("account");
        doReturn(Optional.of(account)).when(accountRepository).findById(transactionRequestDTO.getAccountId());
        Merchant merchant = Fixture.from(Merchant.class).gimme("merchant-meal");
        doReturn(Optional.of(merchant)).when(merchantService).findByName(any());
        doReturn(merchant).when(transactionService).upsertMerchant(any(), any());

        TransactionResponseDTO transactionResponseDTO = transactionService.authorize(transactionRequestDTOSpy);

        assertEquals("00", transactionResponseDTO.getCode());
        verify(accountRepository, times(1)).findById(transactionRequestDTO.getAccountId());
        verify(transactionRequestDTOSpy, times(1)).setMcc(
                String.valueOf(merchant.getMccsFromCategory().stream().findFirst().orElse(null)));
        verify(categoryRepository, never()).findById(any());
        verify(transactionService, times(1)).processTransaction(any(), any(), any(), any());
    }

    @Test
    public void testDoNotFindMerchantButAuthorizeTransactionByMcc() {
        TransactionRequestDTO transactionRequestDTO = Fixture.from(TransactionRequestDTO.class).gimme("valid-meal");
        TransactionRequestDTO transactionRequestDTOSpy = Mockito.spy(transactionRequestDTO);
        Account account = Fixture.from(Account.class).gimme("account");
        doReturn(Optional.of(account)).when(accountRepository).findById(transactionRequestDTO.getAccountId());
        doReturn(Optional.empty()).when(merchantService).findByName(any());
        doReturn(Optional.of(new Category())).when(categoryRepository).findById(transactionRequestDTO.getMcc());
        doReturn(Fixture.from(Merchant.class).gimme("merchant-meal")).when(transactionService).upsertMerchant(any(), any());

        TransactionResponseDTO transactionResponseDTO = transactionService.authorize(transactionRequestDTOSpy);

        assertEquals("00", transactionResponseDTO.getCode());
        verify(accountRepository, times(1)).findById(transactionRequestDTO.getAccountId());
        verify(transactionRequestDTOSpy, never()).setMcc(any());
        verify(categoryRepository, times(1)).findById(transactionRequestDTO.getMcc());
        verify(transactionService, times(1)).processTransaction(any(), any(), any(), any());
    }

    @Test
    public void testProcessTransaction() {
        TransactionRequestDTO transactionRequestDTO = Fixture.from(TransactionRequestDTO.class).gimme("valid-food");
        Account account = Fixture.from(Account.class).gimme("account");
        AccountCategory accountCategory = Fixture.from(AccountCategory.class).gimme("account-category-food");
        Merchant merchant = Fixture.from(Merchant.class).gimme("merchant-food");
        Account accountSpy = Mockito.spy(account);
        AccountCategory accountCategorySpy = Mockito.spy(accountCategory);

        TransactionResponseDTO transactionResponseDTO =
                transactionService.processTransaction(transactionRequestDTO, accountSpy, accountCategorySpy, merchant);

        assertEquals("00", transactionResponseDTO.getCode());
        verify(accountCategorySpy, times(1)).setTotalAmount(
                accountCategory.getTotalAmount() - transactionRequestDTO.getTotalAmount()
        );
        verify(accountSpy, times(1)).setUpdatedAt(any());
        verify(accountRepository, times(1)).save(any());
        verify(transactionRepository, times(1)).save(any());
    }
}
