package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Set;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private AccountContactRepository accountContactRepository;

    @Transactional
    @PostMapping(path = "/api/clients/current/transactions")
    public ResponseEntity<Object> generateTransaction(Authentication authentication, @RequestParam double amount,
                                                      @RequestParam String description, @RequestParam String originAccount,
                                                      @RequestParam String destinationAccount, @RequestParam ContactType saveContact){
        if(description.isEmpty() || amount < 1 || originAccount.isEmpty() || destinationAccount.isEmpty()){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        String clientEmail = accountService.getByNumber(originAccount).getClient().getEmail();
        if(!clientEmail.equals(authentication.getName())){
            return new ResponseEntity<>("You are trying to send money from an account which is not your property. Be sure next time :)", HttpStatus.FORBIDDEN);
        }
        if(accountService.getByNumber(destinationAccount) == null){
            return new ResponseEntity<>("Destiny account was not found", HttpStatus.FORBIDDEN);
        }
        if(accountService.getByNumber(originAccount).getBalance() < amount){
            return new ResponseEntity<>("Insufficient founds", HttpStatus.FORBIDDEN);
        }
        if(accountService.getByNumber(originAccount).isDeleted()){
            return new ResponseEntity<>("Origin account is no longer available", HttpStatus.FORBIDDEN);
        }
        if(accountService.getByNumber(destinationAccount) == accountService.getByNumber(originAccount)){
            return new ResponseEntity<>("Impossible transfer to same account", HttpStatus.FORBIDDEN);
        }
        if(accountService.getByNumber(destinationAccount).isDeleted()){
            return new ResponseEntity<>("Destination account is no longer available", HttpStatus.FORBIDDEN);
        }
        if(saveContact != ContactType.CANCEL) {
            if(contactRepository.findByAccountNumber(destinationAccount) == null){
                Contact contact = new Contact(accountService.getByNumber(destinationAccount).getClient().getFirstName() + " " + accountService.getByNumber(destinationAccount).getClient().getLastName(), destinationAccount);
                AccountContact accountContact = new AccountContact(saveContact);
                accountService.getByNumber(originAccount).addAccountContact(accountContact);
                contact.addAccountContact(accountContact);
                contactRepository.save(contact);
                accountContactRepository.save(accountContact);
            }else{
                AccountContact accountContact = new AccountContact(saveContact);
                accountService.getByNumber(originAccount).addAccountContact(accountContact);
                contactRepository.findByAccountNumber(originAccount).addAccountContact(accountContact);
                accountContactRepository.save(accountContact);
            }
        }

        Transaction debitTransaction = new Transaction(TransactionType.DEBIT, description + " " + "(" + destinationAccount + ")", -(amount), LocalDateTime.now());
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT, description + " " + "(" + originAccount + ")", amount, LocalDateTime.now());

        accountService.getByNumber(originAccount).addTransaction(debitTransaction);
        accountService.getByNumber(destinationAccount).addTransaction(creditTransaction);

        accountService.getByNumber(originAccount).setBalance(accountService.getByNumber(originAccount).getBalance() + -(amount));
        accountService.getByNumber(destinationAccount).setBalance(accountService.getByNumber(destinationAccount).getBalance() + amount);

        transactionService.saveTransaction(debitTransaction);
        transactionService.saveTransaction(creditTransaction);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
