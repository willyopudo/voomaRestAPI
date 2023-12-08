package com.example.voomacrud.services;

import com.example.voomacrud.entity.Card;
import com.example.voomacrud.repository.CardRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Service
public class CardService {
    @Autowired
    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {

        this.cardRepository = cardRepository;
    }

    public ResponseEntity<Card> saveCard(Card card) {
        Card newCard = cardRepository.save(card);
        return ResponseEntity.ok(newCard);
    }
    // Get all cards
    public ResponseEntity<List<Card>> fetchAllCards() {
        return ResponseEntity.ok(cardRepository.findAll());
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
        Card Existingcard = cardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
        Existingcard.setCardAlias(updatedCard.getCardAlias());
        Card savedEntity = cardRepository.save(Existingcard);
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
}
