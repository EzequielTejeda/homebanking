package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionService {

    List<Transaction> getAll();

    Optional<Transaction> getById(Long id);

    Transaction saveTransaction(Transaction transaction);
}
