package com.transactionAssignment.app.service;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.transactionAssignment.app.model.Merchant;
import com.transactionAssignment.app.repository.MerchantRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MerchantServiceTest {
    @InjectMocks
    MerchantService merchantService;

    @Mock
    MerchantRepository merchantRepository;

    @BeforeAll
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("com.transactionAssignment.app.fixtures");
    }

    @Test
    public void testReturnMerchantByItsName() {
        String name = "meal merchant";
        Merchant merchant = Fixture.from(Merchant.class).gimme("merchant-meal");
        doReturn(Optional.of(merchant)).when(merchantRepository).findByName(name);

        Optional<Merchant> merchantReturn = merchantService.findByName(name);

        assertTrue(merchantReturn.isPresent());
        assertEquals(merchantReturn.get().getId(), merchant.getId());
        verify(merchantRepository, times(1)).findByName(name);
    }

    @Test
    public void testReturnEmptyMerchant() {
        String emptyName = "";
        doReturn(Optional.empty()).when(merchantRepository).findByName(emptyName);

        Optional<Merchant> emptyMerchant = merchantService.findByName(emptyName);

        assertFalse(emptyMerchant.isPresent());
        verify(merchantRepository, times(1)).findByName(emptyName);
    }

    @Test
    public void testCreateNewMerchantFromNameAndCategory() {
        String name = "new merchant name";
        UUID categoryId = UUID.randomUUID();
        doReturn(Optional.empty()).when(merchantRepository).findByName(name);

        merchantService.upsertMerchant(name, categoryId);

        verify(merchantRepository, times(1)).findByName(name);
        verify(merchantRepository, times(1)).save(any(Merchant.class));
    }

    @Test
    public void testUpdateExistingMerchantWithNewCategory() {
        String name = "meal merchant";
        UUID categoryId = UUID.randomUUID();
        Merchant existingMerchant = Fixture.from(Merchant.class).gimme("merchant-meal");
        doReturn(Optional.of(existingMerchant)).when(merchantRepository).findByName(name);

        merchantService.upsertMerchant(name, categoryId);

        assertEquals(categoryId, existingMerchant.getCategoryId());
        assertNotNull(existingMerchant.getUpdatedAt());
        verify(merchantRepository, times(1)).findByName(name);
        verify(merchantRepository, times(1)).save(existingMerchant);
    }
}
