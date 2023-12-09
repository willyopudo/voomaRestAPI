package com.example.voomacrud.dto;

import com.example.voomacrud.entity.Card;
import lombok.*;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountCardsDto {
	private String iban;
	private String bankCode;
	private int customerId;
	Optional<List<CardDto>> cards;
}
