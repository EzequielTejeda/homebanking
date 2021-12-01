package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;

import java.util.List;
import java.util.Optional;

public interface CardService {

    List<Card> getAll();

    Optional<Card> getById(Long id);

    Card saveCard(Card card);

    Card getByNumber(String number);
}
