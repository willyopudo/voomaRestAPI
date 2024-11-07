package com.example.voomacrud.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDto {
    private long id;
    private String iban;
    private String bankCode;
    private int customerId;
}
