package com.mindhub.homebanking.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {

    private Long id;
    private String number;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy HH:mm")
    private LocalDateTime creationDate;
    private double balance;
    private AccountType accountType;
    private boolean deleted;
    private Set<TransactionDTO> transactions;
    private Set<AccountContactDTO> accountContacts;

    public AccountDTO(Account account) {

        this.id = account.getId();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.accountType = account.getAccountType();
        this.deleted = account.isDeleted();
        this.transactions = account.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toSet());
        this.accountContacts = account.getAccountContacts().stream().map(AccountContactDTO::new).collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
    public AccountType getAccountType() {
        return accountType;
    }
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }
    public void setTransactions(Set<TransactionDTO> transactions) {
        this.transactions = transactions;
    }
    public Set<AccountContactDTO> getAccountContacts() {
        return accountContacts;
    }
    public void setAccountContacts(Set<AccountContactDTO> accountContacts) {
        this.accountContacts = accountContacts;
    }
}
