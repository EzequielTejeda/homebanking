package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String name;
    private String accountNumber;

    @OneToMany(mappedBy = "contact", fetch = FetchType.EAGER)
    Set<AccountContact> accountContacts = new HashSet<>();

    public Contact() {
    }

    public Contact(String name, String accountNumber) {
        this.name = name;
        this.accountNumber = accountNumber;
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    public Set<AccountContact> getAccountContacts() {
        return accountContacts;
    }
    public void setAccountContacts(Set<AccountContact> accountContacts) {
        this.accountContacts = accountContacts;
    }
    public void addAccountContact(AccountContact accountContact){
        accountContact.setContact(this);
        accountContacts.add(accountContact);
    }
    @JsonIgnore
    public List<Account> getAccounts(){
        return accountContacts.stream().map(AccountContact::getAccount).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                '}';
    }
}
