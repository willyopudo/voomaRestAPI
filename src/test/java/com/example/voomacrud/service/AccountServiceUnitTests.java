package com.example.voomacrud.service;

import com.example.voomacrud.dto.AccountDto;
import com.example.voomacrud.entity.Account;
import com.example.voomacrud.repository.AccountRepository;
import com.example.voomacrud.services.AccountService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountServiceUnitTests {
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountService accountService;

    ResponseEntity<AccountDto> accountResp;
    AccountDto accountDto;
    Account account;

    @BeforeEach
    public void setUp() {
        accountDto = AccountDto.builder()
                .id(1L)
                .iban("10008456300")
                .bankCode("BNK002")
                .customer(12)
                .build();
        accountResp = ResponseEntity.ok(accountDto);

        account = Account.builder()
                .id(1L)
                .iban("346540863137")
                .bankCode("BNK001")
                .customerId(12)
                .build();
    }

    @Test
    @Order(1)
    public void saveAccountTest(){
        // precondition
        given(accountRepository.save(any())).willReturn(account);

        //action
        ResponseEntity<AccountDto> savedAccount = accountService.saveAccount(accountDto);

        // verify the output
        System.out.println(savedAccount.getBody().getIban());
        Assertions.assertNotNull(savedAccount);
    }
}
