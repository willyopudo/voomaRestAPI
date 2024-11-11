package com.example.voomacrud.services;

import com.example.voomacrud.dto.CardDto;
import com.example.voomacrud.entity.Card;
import com.example.voomacrud.repository.AccountRepository;
import com.example.voomacrud.repository.CardRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CardService {
    @Autowired
    private final CardRepository cardRepository;
    @Autowired
    private AccountRepository accountRepository;


    public CardService(CardRepository cardRepository) {

        this.cardRepository = cardRepository;

    }

    public ResponseEntity<CardDto> saveCard(CardDto cardDto) {
        Card newCard = cardRepository.save(cardDtoToCard(cardDto));
        return ResponseEntity.ok(cardToCardDto(newCard));
    }
    // Get all cards
    public Page<CardDto> fetchAllCards(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return cardRepository.findAll(pageable).map(CardDto::new);
    }

    public ResponseEntity<Optional<Card>> fetchCardById(Long id){
        Optional<Card> card = cardRepository.findById(id);
        if(card.isPresent())
            return ResponseEntity.ok(card);
        else
            return ResponseEntity.notFound().build();
    }
    public ResponseEntity<Card> updateCard(Long id, Card updatedCard){
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if(!Objects.equals(updatedCard.getId(), id))
            return ResponseEntity.status(400).build();
        Card ExistingCard = cardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
        ExistingCard.setCardAlias(updatedCard.getCardAlias());
        Card savedEntity = cardRepository.save(ExistingCard);
        return ResponseEntity.ok(savedEntity);
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
    private CardDto cardToCardDto(Card card){
        CardDto cardDto = new CardDto();
        cardDto.setId(card.getId());
        cardDto.setCardAlias(card.getCardAlias());
        cardDto.setCardType(card.getCardType());
        cardDto.setAccountId(card.getAccount().getId());

        return cardDto;
    }

    private Card cardDtoToCard(CardDto cardDto){
        Card card = new Card();
        card.setCardAlias(cardDto.getCardAlias());
        card.setCardType(cardDto.getCardType());
        card.setAccount(accountRepository.getReferenceById(cardDto.getAccountId()));

        return card;
    }

    private Optional<List<CardDto>> cardListToDto(List<Card> cards){
        return Optional.of(cards.stream().map(
		        this::cardToCardDto).collect(Collectors.toList()));

    }


}
