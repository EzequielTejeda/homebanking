package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    Account getByNumber(String number);

    List<Account> getAll();

    Account saveAccount(Account account);

    Optional<Account> getById(Long id);
}
