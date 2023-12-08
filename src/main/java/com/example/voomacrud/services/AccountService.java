package com.example.voomacrud.services;

import com.example.voomacrud.entity.Account;
import com.example.voomacrud.repository.AccountRepository;
import com.example.voomacrud.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {

            this.accountRepository = accountRepository;
    }

    public ResponseEntity<Account> saveAccount(Account account) {
        Account newAccount = accountRepository.save(account);
        return ResponseEntity.ok(newAccount);
    }
    // Get all accounts
    public ResponseEntity<List<Account>> fetchAllAccounts() {
        return ResponseEntity.ok(accountRepository.findAll());
    }

    public ResponseEntity<Optional<Account>> fetchAccountById(Long id){
        Optional<Account> account = accountRepository.findById(id);
        if(account.isPresent())
            return ResponseEntity.ok(account);
        else
            return ResponseEntity.notFound().build();
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
}
