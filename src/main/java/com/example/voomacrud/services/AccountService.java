package com.example.voomacrud.services;

import com.example.voomacrud.dto.AccountCardsDto;
import com.example.voomacrud.dto.AccountDto;
import com.example.voomacrud.dto.CardDto;
import com.example.voomacrud.entity.Account;
import com.example.voomacrud.entity.Card;
import com.example.voomacrud.mapper.CardMapper;
import com.example.voomacrud.repository.AccountRepository;
import com.example.voomacrud.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.voomacrud.util.specifications.AccountSpecification.*;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    private final CardService cardService;

    //private final CardMapper cardMapper;

    @Autowired
    public AccountService(AccountRepository accountRepository, CardService cardService) {
        this.accountRepository = accountRepository;
        this.cardService = cardService;
        //this.cardMapper = cardMapper;
    }

    public ResponseEntity<AccountDto> saveAccount(AccountDto accountDto) {
        Account newAccount = accountRepository.save(accountDtoToAccount(accountDto));
        return ResponseEntity.ok(accountToAccountDto(newAccount));
    }

    public ResponseEntity<AccountDto> updateAccount(AccountDto accountDto,long id) {
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(RuntimeException::new);

        existingAccount.setIban(accountDto.getIban());
        existingAccount.setBankCode(accountDto.getBankCode());
        existingAccount.setCustomerId(accountDto.getCustomer());


        accountRepository.save(existingAccount);
        return ResponseEntity.ok(accountToAccountDto(existingAccount));
    }
    // Get all accounts
    public ResponseEntity<List<AccountDto>> fetchAllAccounts(String ibanLikeFilter, String cardType, List<Integer> customers) {
        // Only create specifications for fields with values:
        Specification<Account> filters = Specification.where(StringUtils.isBlank(ibanLikeFilter) ? null : ibanLike(ibanLikeFilter))
                .and(StringUtils.isBlank(cardType) ? null : hasCardsWithType(cardType))
                .and(CollectionUtils.isEmpty(customers) ? null : withCustomer(customers));

        // User our created Specification to query our repository
        return ResponseEntity.ok(accountListToDto(accountRepository.findAll(filters)).orElse(new ArrayList<>()));
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
        Optional<List<CardDto>> cards = cardService.cardListToDto(account.getCards());

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
        accountDto.setCustomer(account.getCustomerId());

        return accountDto;
    }

    private Account accountDtoToAccount(AccountDto accountDto){
        Account account = new Account();
        account.setId(accountDto.getId());
        account.setIban(accountDto.getIban());
        account.setBankCode(accountDto.getBankCode());
        account.setCustomerId(accountDto.getCustomer());

        return account;
    }

    private Optional<List<AccountDto>> accountListToDto(List<Account> accounts){
        return Optional.of(accounts.stream().map(
                this::accountToAccountDto).collect(Collectors.toList()));

    }
}
