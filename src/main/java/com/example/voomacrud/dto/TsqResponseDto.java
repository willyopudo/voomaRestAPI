package com.example.voomacrud.dto;

import java.time.LocalDateTime;

public record TsqResponseDto(

         String transRef,

         Double amount,

         String accountNumber,

         String payerName,

         String payerPhone,

         String paymentMode,

         String narration,

         String channel,

         LocalDateTime transDate,

         String transType

         ) {
}
