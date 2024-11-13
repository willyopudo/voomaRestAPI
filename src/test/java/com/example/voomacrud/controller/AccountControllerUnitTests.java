package com.example.voomacrud.controller;

import com.example.voomacrud.dto.AccountDto;
import com.example.voomacrud.entity.Account;
import com.example.voomacrud.services.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountControllerUnitTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountService accountService;
    @Autowired
    private ObjectMapper objectMapper;

    ResponseEntity<AccountDto> account;
    AccountDto accountDto;

    @BeforeEach
    public void setUp() {
        accountDto = AccountDto.builder()
                .id(1L)
                .iban("10008456300")
                .bankCode("BNK002")
                .customer(12)
                .build();
        account = ResponseEntity.ok(accountDto);
    }

    //Post Controller
    @Test
    @Order(1)
    public void saveAccountTest() throws Exception {
        //Precondition
        given(accountService.saveAccount(any(AccountDto.class))).willReturn(account);

        // action
        ResultActions response = mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(account)));

        //verify
        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.iban", is(account.getBody().getIban())))
                .andExpect(jsonPath("$.bankCode", is(account.getBody().getBankCode())))
                .andExpect(jsonPath("$.customer", is(account.getBody().getCustomer())));
    }
    //Get All Controller
    @Test
    @Order(2)
    public void getAccountTest() throws Exception {
        //Precondition
        List<AccountDto> accountList = new ArrayList<>();
        accountList.add(accountDto);
        accountList.add(AccountDto.builder().id(2L).iban("100200300").bankCode("BNK003").customer(20).build());

        given(accountService.fetchAllAccounts(null, null, null)).willReturn(ResponseEntity.ok(accountList));

        //action
        ResultActions response = mockMvc.perform(get("/api/v1/accounts"));

        // verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(accountList.size())));
    }
    //get by Id controller
    @Test
    @Order(3)
    public void getByIdAccountTest() throws Exception{
        // precondition
        given(accountService.fetchAccountById(account.getBody().getId())).willReturn(account);

        // action
        ResultActions response = mockMvc.perform(get("/api/v1/accounts/{id}", account.getBody().getId()));

        // verify
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.iban", is(account.getBody().getIban())))
                .andExpect(jsonPath("$.bankCode", is(account.getBody().getBankCode())))
                .andExpect(jsonPath("$.customer", is(account.getBody().getCustomer())));

    }

    //Update Account
    @Test
    @Order(4)
    public void updateAccountTest() throws Exception{
        // precondition
        given(accountService.fetchAccountById(account.getBody().getId())).willReturn(account);
        account.getBody().setBankCode("BNK009");
        account.getBody().setIban("90010028389");
        given(accountService.updateAccount(any(AccountDto.class), eq(account.getBody().getId()))).willReturn(account);

        // action
        ResultActions response = mockMvc.perform(put("/api/v1/accounts/{id}", account.getBody().getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(account)));

        // verify
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.iban", is(account.getBody().getIban())))
                .andExpect(jsonPath("$.bankCode", is(account.getBody().getBankCode())));
    }

}
