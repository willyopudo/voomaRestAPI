package com.example.voomacrud.controller;

import com.example.voomacrud.entity.Card;
import com.example.voomacrud.services.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CardController {
    @Autowired
    private final CardService cardService;
    // Create a new card
    @PostMapping("/card")
    public ResponseEntity<Card> saveCard(@RequestBody Card card) {
        ResponseEntity<Card> newCard = cardService.saveCard(card);
        URI uri = URI.create("/card/" + Objects.requireNonNull(newCard.getBody()).getId());
        return ResponseEntity.created(uri).body(newCard.getBody());
    }

    // Get all cards
    @GetMapping("/card")
    public ResponseEntity<Page<Card>> getAllCards(@RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "4") int pageSize) {
        Page<Card> cards = cardService.fetchAllCards(pageNo, pageSize);
        return ResponseEntity.ok(cards);
    }
    // Get a card by ID
    @GetMapping("/card/{id}")
    public ResponseEntity<Optional<Card>> getCardById(@PathVariable Long id) {
        ResponseEntity<Optional<Card>> card = cardService.fetchCardById(id);
        return Objects.requireNonNullElseGet(card, () -> ResponseEntity.notFound().build());
    }

     //Update Card
    @PutMapping(path = "/card/{cardId}")
    public ResponseEntity<Card> updateCard(@PathVariable(value = "cardId") Long cardId, @RequestBody Card card)
    {
        return cardService.updateCard(cardId,card);
    }

    //Delete a card
    @DeleteMapping(value = "/card/{cardId}")
    public ResponseEntity<String> deleteCard(@PathVariable Long cardId) {
        return cardService.deleteCard(cardId);

    }
}
