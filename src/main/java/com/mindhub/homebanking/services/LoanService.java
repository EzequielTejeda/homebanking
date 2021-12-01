package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanService {

    List<Loan> getAll();

    Optional<Loan> getById(Long id);

    Loan saveLoan(Loan loan);
}
