package com.example.voomacrud.dto;

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
}
