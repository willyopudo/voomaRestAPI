package com.example.voomacrud.repository;

import com.example.voomacrud.entity.Account;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountRepositoryUnitTests {
    @Autowired
    private AccountRepository accountRepository;

    @Test
    @DisplayName("Test 1:Save Account Test")
    @Order(1)
    @Rollback(value = false)
    public void testSaveAccount() {
        Account account = Account.builder()
                .iban("346540863137")
                .bankCode("BNK001")
                .customerId(12)
                .build();
        accountRepository.save(account);

        System.out.println(account.getId() + " " + account.getIban() + " " + account.getBankCode() + " " + account.getCustomerId());
        Assertions.assertNotNull(account.getId());

    }
    @Test
    @Order(2)
    public void testGetAccount() {
        Account account = accountRepository.findById(1L).get();

        System.out.println(account.getIban() + " " + account.getBankCode() + " " + account.getCustomerId());
        Assertions.assertEquals(account.getId(), 1L);
    }

    @Test
    @Order(3)
    public void getListOfAccountsTest(){
        //Action
        List<Account> accounts = accountRepository.findAll();
        //Verify
        System.out.println(accounts);
        Assertions.assertFalse(accounts.isEmpty());

    }

    @Test
    @Order(4)
    @Rollback(value = false)
    public void updateAccountTest(){

        //Action
        Account account = accountRepository.findById(1L).get();
        account.setBankCode("BNK031");
        Account accountUpdated =  accountRepository.save(account);

        //Verify
        System.out.println(accountUpdated.getIban() + " " + accountUpdated.getBankCode() + " " + accountUpdated.getCustomerId());
        Assertions.assertEquals("BNK031", accountUpdated.getBankCode());

    }

    @Test
    @Order(5)
    @Rollback(value = false)
    public void deleteAccountTest(){
        //Action
        accountRepository.deleteById(1L);
        Optional<Account> accountOptional = accountRepository.findById(1L);

        //Verify
        Assertions.assertTrue(accountOptional.isEmpty());
    }
}
