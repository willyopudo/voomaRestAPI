package com.example.voomacrud.dto;

import com.example.voomacrud.entity.Card;
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
	private byte cardType;
	private Long accountId;

	public CardDto(Card card) {
		this.cardAlias = card.getCardAlias();
		this.cardType = card.getCardType();
		this.id = card.getId();
		this.accountId = card.getAccount().getId();
	}
}
