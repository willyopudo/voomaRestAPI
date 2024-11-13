package com.example.voomacrud.mapper;

import com.example.voomacrud.dto.CardDto;
import com.example.voomacrud.entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CardMapper {
    CardDto toCardDto(Card card);
    List<CardDto> toCardDtos(List<Card> cards);
}
