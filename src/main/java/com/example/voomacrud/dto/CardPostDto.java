package com.example.voomacrud.dto;

import com.example.voomacrud.enums.CardType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardPostDto {
    private Long id;
    private String cardAlias;
    private CardType cardType; //CREDIT, DEBIT, PREPAID, ATM, WALLET
    private Long account;
}
