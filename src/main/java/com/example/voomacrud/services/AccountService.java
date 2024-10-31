package com.example.voomacrud.services;

import com.example.voomacrud.dto.AccountCardsDto;
import com.example.voomacrud.dto.AccountDto;
import com.example.voomacrud.dto.CardDto;
import com.example.voomacrud.entity.Account;
import com.example.voomacrud.entity.Card;
import com.example.voomacrud.repository.AccountRepository;
import com.example.voomacrud.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    private final CardService cardService;

    public AccountService(AccountRepository accountRepository, CardService cardService) {

            this.accountRepository = accountRepository;
            this.cardService = cardService;
    }

    public ResponseEntity<AccountDto> saveAccount(Account account) {
        Account newAccount = accountRepository.save(account);
        return ResponseEntity.ok(accountToAccountDto(newAccount));
    }
    // Get all accounts
    public ResponseEntity<List<AccountDto>> fetchAllAccounts() {
        return ResponseEntity.ok(accountListToDto(accountRepository.findAll()).orElse(new ArrayList<>()));
    }

    public ResponseEntity<AccountDto> fetchAccountById(Long id){
        Account account = accountRepository.findById(id).orElse(null);
        if(account != null)
            return ResponseEntity.ok(accountToAccountDto(account));
        else
            return ResponseEntity.notFound().build();
    }

    public AccountCardsDto findAccountWithCards(Long accountId) {
        var account = accountRepository.findById(accountId)
                .orElse(
                        Account.builder()
                                .id(0L)
                                .iban("NOT_FOUND")
                                .bankCode("NOT_FOUND")
                                .customerId(0)
                                .build()
                );
        Optional<List<CardDto>> cards = cardService.findAllCardsByAccountId(accountId);

        return AccountCardsDto.builder()
                .iban(account.getIban())
                .bankCode(account.getBankCode())
                .customerId(account.getCustomerId())
                .cards(cards)
                .build();
    }
//    public ResponseEntity<Account> updateAccount(Long id, Account updatedAccount){
//        if (id == null) {
//            throw new IllegalArgumentException("ID cannot be null");
//        }
//
//    }

    public ResponseEntity<String> deleteAccount(Long id) {
        Optional<Account> existingAccount = accountRepository.findById(id);
        if(existingAccount.isPresent()) {
            accountRepository.deleteById(id);
            return ResponseEntity.ok("Account Deleted Successfully against id " + id + " ");
        }
        else
            return ResponseEntity.notFound().build();
    }

    private AccountDto accountToAccountDto(Account account){
        AccountDto accountDto = new AccountDto();
        accountDto.setId(account.getId());
        accountDto.setIban(account.getIban());
        accountDto.setBankCode(account.getBankCode());
        accountDto.setCustomerId(account.getCustomerId());

        return accountDto;
    }

    private Optional<List<AccountDto>> accountListToDto(List<Account> accounts){
        return Optional.of(accounts.stream().map(
                this::accountToAccountDto).collect(Collectors.toList()));

    }
}
