package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Account {//ENCABEZADO

    //ATRIBUTOS
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;
    private AccountType accountType;
    private boolean deleted;

    //RELACION ENTRE ENTIDADES
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    Set<Transaction> transactions = new HashSet<>();

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    Set<AccountContact> accountContacts = new HashSet<>();

    //CONSTRUCTORES
    public Account() { }

    public Account(String number, LocalDateTime creationDate, double balance, AccountType accountType, boolean deleted) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.accountType = accountType;
        this.deleted = deleted;
    }

    //ACCESADORES Y MUTANTES

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
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }
    public Set<Transaction> getTransactions() {
        return transactions;
    }
    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }
    public Set<AccountContact> getAccountContacts() {
        return accountContacts;
    }
    public void setAccountContacts(Set<AccountContact> accountContacts) {
        this.accountContacts = accountContacts;
    }

    public void addTransaction(Transaction transaction){
        transaction.setAccount(this);
        transactions.add(transaction);
    }
    public void addAccountContact(AccountContact accountContact){
        accountContact.setAccount(this);
        accountContacts.add(accountContact);
    }
    public List<Contact> getContacts(){
        return accountContacts.stream().map(AccountContact::getContact).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", creationDate=" + creationDate +
                ", balance=" + balance +
                ", accountType=" + accountType +
                ", deleted=" + deleted +
                ", client=" + client +
                ", transactions=" + transactions +
                ", accountContacts=" + accountContacts +
                '}';
    }
}
