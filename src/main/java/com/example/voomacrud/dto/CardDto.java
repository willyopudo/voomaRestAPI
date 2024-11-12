package com.example.voomacrud.dto;

import com.example.voomacrud.entity.Card;
import com.example.voomacrud.enums.CardType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {
	private Long id;
	private String cardAlias;
	private CardType cardType; //CREDIT, DEBIT, PREPAID, ATM, WALLET
	private String account;

	public CardDto(Card card) {
		this.cardAlias = card.getCardAlias();
		this.cardType = CardType.getByIntValue(card.getCardType());
		this.id = card.getId();
		this.account = card.getAccount().getIban();
	}
}
