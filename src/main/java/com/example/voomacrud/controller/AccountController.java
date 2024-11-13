package com.example.voomacrud.controller;

import com.example.voomacrud.dto.AccountCardsDto;
import com.example.voomacrud.dto.AccountDto;
import com.example.voomacrud.entity.Account;
import com.example.voomacrud.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/accounts")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class AccountController {
    @Autowired
    private final AccountService accountService;
    //private ModelMapper modelMapper;

    // Create a new account
    @PostMapping
    public ResponseEntity<AccountDto> saveAccount(@RequestBody AccountDto account) {
        ResponseEntity<AccountDto> newAccount = accountService.saveAccount(account);
        URI uri = URI.create("/" + Objects.requireNonNull(newAccount.getBody()).getId());
        return ResponseEntity.created(uri).body(newAccount.getBody());
    }

    // Get all accounts
    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts(@RequestParam(required = false) String ibanLike, @RequestParam(required = false) String cardType, @RequestParam(required = false) List<Integer> customers) {
        return accountService.fetchAllAccounts(ibanLike, cardType, customers);
    }
    // Get a account by ID
    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Long id) {
        ResponseEntity<AccountDto> account = accountService.fetchAccountById(id);
        return Objects.requireNonNullElseGet(account, () -> ResponseEntity.notFound().build());
    }
    @GetMapping("/{id}/cards")
    public ResponseEntity<AccountCardsDto> findAllCardsByAccount(
            @PathVariable("id") Long accountId
    ) {
        return ResponseEntity.ok(accountService.findAccountWithCards(accountId));
    }

     //Update an account
    @PutMapping("/{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable("id") Long accountId, @RequestBody AccountDto account)
    {
        return accountService.updateAccount(account, accountId);
    }

    //Delete a account
    @DeleteMapping(value = "{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id) {
        return accountService.deleteAccount(id);

    }



}
