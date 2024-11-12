package com.example.voomacrud.controller;

import com.example.voomacrud.dto.CardDto;
import com.example.voomacrud.dto.CardPostDto;
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
@RequestMapping("/api/v1/cards")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class CardController {
    @Autowired
    private final CardService cardService;
    // Create a new card
    @PostMapping
    public ResponseEntity<CardDto> addCard(@RequestBody CardPostDto cardDto) {
        ResponseEntity<CardDto> newCard = cardService.saveCard(cardDto);
        URI uri = URI.create("/" + Objects.requireNonNull(newCard.getBody()).getId());
        return ResponseEntity.created(uri).body(newCard.getBody());
    }

    // Get all cards
    @GetMapping
    public ResponseEntity<Page<CardDto>> getAllCards(@RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "4") int pageSize) {
        Page<CardDto> cards = cardService.fetchAllCards(pageNo, pageSize);
        return ResponseEntity.ok(cards);
    }
    // Get a card by ID
    @GetMapping("/{id}")
    public ResponseEntity<Optional<CardDto>> getCardById(@PathVariable Long id) {
        ResponseEntity<Optional<CardDto>> card = cardService.fetchCardById(id);
        return Objects.requireNonNullElseGet(card, () -> ResponseEntity.notFound().build());
    }

     //Update Card
    @PutMapping(path = "/{id}")
    public ResponseEntity<CardDto> updateCard(@PathVariable(value = "id") Long id, @RequestBody CardDto card)
    {
        return cardService.updateCard(id,card);
    }

    //Delete a card
    @DeleteMapping(value = "/{cardId}")
    public ResponseEntity<String> deleteCard(@PathVariable Long cardId) {
        return cardService.deleteCard(cardId);

    }
}
