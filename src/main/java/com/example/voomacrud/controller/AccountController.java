package com.example.voomacrud.controller;

import com.example.voomacrud.dto.AccountCardsDto;
import com.example.voomacrud.entity.Account;
import com.example.voomacrud.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    @Autowired
    private final AccountService accountService;
    //private ModelMapper modelMapper;

    // Create a new account
    @PostMapping
    public ResponseEntity<Account> saveAccount(@RequestBody Account account) {
        ResponseEntity<Account> newAccount = accountService.saveAccount(account);
        URI uri = URI.create("/account/" + Objects.requireNonNull(newAccount.getBody()).getId());
        return ResponseEntity.created(uri).body(newAccount.getBody());
    }

    // Get all accounts
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        return accountService.fetchAllAccounts();
    }
    // Get a account by ID
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Account>> getAccountById(@PathVariable Long id) {
        ResponseEntity<Optional<Account>> account = accountService.fetchAccountById(id);
        if (account != null) {
            return account;
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/cards/{accountId}")
    public ResponseEntity<AccountCardsDto> findAllCardsByAccount(
            @PathVariable("accountId") Long accountId
    ) {
        return ResponseEntity.ok(accountService.findAccountWithCards(accountId));
    }

    // Update a account
//    @PutMapping(path = "/account/{accountId}")
//    public ResponseEntity<Account> updateAccount(@PathVariable(value = "accountId") Long accountId, @RequestBody Account account)
//    {
//        return accountService.updateAccount(accountId,account);
//    }

    //Delete a account
    @DeleteMapping(value = "{accountId}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long accountId) {
        return accountService.deleteAccount(accountId);

    }



}
