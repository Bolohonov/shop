package org.example.payment.repo;

import org.example.payment.CreateTestAccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.example.payment.config.TestcontainersConfiguration;
import org.example.payment.model.Account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class AccountRepositoryTest extends TestcontainersConfiguration {

    @Autowired
    private AccountRepository accountRepository;

    @DisplayName("Тестирование сохранения аккаунта в репозитории")
    @Test
    void testSaveAccount() {
        Account account = accountRepository.save(CreateTestAccountService.createDefaultAccount()).block();

        assertNotNull(account);
        assertNotNull(account.getId());
        assertEquals(5000.0, account.getBalance());
    }

    @DisplayName("Тестирование чтения аккаунта из репозитория")
    @Test
    void testSaveAndReadAccount() {
        Account account = accountRepository.save(CreateTestAccountService.createDefaultAccount()).block();

        Account foundAccount = accountRepository.findById(account.getId()).block();

        assertNotNull(foundAccount);
        assertEquals(5000.0, foundAccount.getBalance());
    }
}
