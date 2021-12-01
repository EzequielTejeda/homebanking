package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class CardController {

    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountController accountController;

    public String getUniqueCardNumber(){
        String uniqueNumber = null;
        do{
            uniqueNumber = accountController.getRandomNumber(1000,9999) + " " + accountController.getRandomNumber(1000,9999) + " " + accountController.getRandomNumber(1000,9999) + " " + accountController.getRandomNumber(1000,9999);
        }
        while (cardService.getByNumber(uniqueNumber) != null);
        return (uniqueNumber);
    }

    @PostMapping(path = "/api/clients/current/cards")
    public ResponseEntity<Object> createCard(@RequestParam CardType type, @RequestParam CardColor color,
                                             Authentication authentication){
        Set<Card> allCards = clientService.getByEmail(authentication.getName()).getCards();
        Set<Card> allValidCards = allCards.stream().filter(card -> !card.isDeleted()).collect(Collectors.toSet());
        if(allValidCards.size() >= 6){
            return new ResponseEntity<>("You own all the cards.", HttpStatus.FORBIDDEN);
        }
        List<Card> allCardsByType = clientService.getByEmail(authentication.getName()).getCards().stream().filter(card -> card.getType() == type).collect(Collectors.toList());
        List<Card> validCards = allCardsByType.stream().filter(card -> !card.isDeleted()).collect(Collectors.toList());
        if(validCards.size() >= 3){
            return new ResponseEntity<>("You have 3 " + type + " cards already!", HttpStatus.FORBIDDEN);
        }
        Card card = new Card(clientService.getByEmail(authentication.getName()).getLastName() + " " + clientService.getByEmail(authentication.getName()).getFirstName(), type, color, getUniqueCardNumber(), accountController.getRandomNumber(100, 999), LocalDateTime.now(), LocalDateTime.now().plusYears(5), false, false);
        clientService.getByEmail(authentication.getName()).addCard(card);
        cardService.saveCard(card);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(path = "/api/clients/current/cards/delete")
    public ResponseEntity<Object> deleteCard(@RequestParam String cardNumber, Authentication authentication){
        if(cardService.getByNumber(cardNumber) == null){
            return new ResponseEntity<>("Card was not found", HttpStatus.NOT_FOUND);
        }
        if(!Objects.equals(cardService.getByNumber(cardNumber).getClient().getEmail(), authentication.getName())){
            return new ResponseEntity<>("This card is not client's property", HttpStatus.FORBIDDEN);
        }

        cardService.getByNumber(cardNumber).setDeleted(true);

        cardService.saveCard(cardService.getByNumber(cardNumber));

        return new ResponseEntity<>("Card successfully deleted!",HttpStatus.ACCEPTED);
    }

    /*@PatchMapping(path = "/api/clients/current/cards/delete")
    public ResponseEntity<Object> deleteCard(@RequestParam String cardThruDate){
        if(cardThruDate != )
        return new ResponseEntity<>(HttpStatus.LOCKED);
    }*/
}