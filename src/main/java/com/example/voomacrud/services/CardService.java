package com.example.voomacrud.services;

import com.example.voomacrud.dto.CardDto;
import com.example.voomacrud.dto.CardPostDto;
import com.example.voomacrud.entity.Card;
import com.example.voomacrud.enums.CardType;
import com.example.voomacrud.repository.AccountRepository;
import com.example.voomacrud.repository.CardRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.voomacrud.util.specifications.AccountSpecification.*;
import static com.example.voomacrud.util.specifications.CardSpecification.withAccountId;
import static com.example.voomacrud.util.specifications.CardSpecification.withType;

@Service
public class CardService {
    @Autowired
    private final CardRepository cardRepository;
    @Autowired
    private AccountRepository accountRepository;


    public CardService(CardRepository cardRepository) {

        this.cardRepository = cardRepository;

    }

    public ResponseEntity<CardDto> saveCard(CardPostDto cardDto) {
        Card newCard = cardRepository.save(cardDtoToCard(cardDto));
        return ResponseEntity.ok(cardToCardDto(newCard));
    }
    // Get all cards
    public Page<CardDto> fetchAllCards(int pageNo, int pageSize, String cardType, List<String> accounts) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        // Only create specifications for fields with values:
        Specification<Card> filters = Specification.where(StringUtils.isBlank(cardType) ? null : withType(cardType))
                .and(CollectionUtils.isEmpty(accounts) ? null : withAccountId(accounts));

        // User our created Specification to query our repository
        return cardRepository.findAll(filters,pageable).map(CardDto::new);
    }

    public ResponseEntity<Optional<CardDto>> fetchCardById(Long id){
        Optional<Card> card = cardRepository.findById(id);
        return card.map(value -> ResponseEntity.ok(Optional.of(cardToCardDto(value)))).orElseGet(() -> ResponseEntity.notFound().build());
    }
    public ResponseEntity<CardDto> updateCard(Long id, CardDto updatedCard){
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if(!Objects.equals(updatedCard.getId(), id))
            return ResponseEntity.status(400).build();
        Card existingCard = cardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
        existingCard.setCardAlias(updatedCard.getCardAlias());
        existingCard.setCardType(updatedCard.getCardType().getIntVal());

        Card savedEntity = cardRepository.save(existingCard);
        return ResponseEntity.ok(cardToCardDto(savedEntity));
    }

    public ResponseEntity<String> deleteCard(Long id) {
        Optional<Card> existingCard = cardRepository.findById(id);
        if(existingCard.isPresent()) {
            cardRepository.deleteById(id);
            return ResponseEntity.ok("Card Deleted Successfully against id " + id + " ");
        }
        else
            return ResponseEntity.notFound().build();
    }

	public Optional<List<CardDto>> findAllCardsByAccountId(Long accountId) {
		return cardListToDto(cardRepository.findAllByAccountId(accountId));

	}
    public CardDto cardToCardDto(Card card){
        CardDto cardDto = new CardDto();
        cardDto.setId(card.getId());
        cardDto.setCardAlias(card.getCardAlias());
        cardDto.setCardType(CardType.getByIntValue(card.getCardType()) );
        cardDto.setAccount(card.getAccount().getIban());

        return cardDto;
    }

    private Card cardDtoToCard(CardPostDto cardDto){
        Card card = new Card();
        card.setCardAlias(cardDto.getCardAlias());
        card.setCardType(cardDto.getCardType().getIntVal());
        card.setAccount(accountRepository.getReferenceById(cardDto.getAccount()));

        return card;
    }

    public Optional<List<CardDto>> cardListToDto(List<Card> cards){
        return Optional.of(cards.stream().map(
		        this::cardToCardDto).collect(Collectors.toList()));

    }


}
