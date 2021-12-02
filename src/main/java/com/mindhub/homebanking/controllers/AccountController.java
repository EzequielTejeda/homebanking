package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;

    public int getRandomNumber(int min, int max){
        return  (int) ((Math.random() * (max - min)) + min);
    }

    public String getUniqueNumber(){
        int uniqueNumber = 0;
        do{
            uniqueNumber = getRandomNumber(100, 99999999);
        }
        while (accountService.getByNumber("VIN" + uniqueNumber) != null);
        return ("VIN" + uniqueNumber) ;
    }

    @GetMapping("/api/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.getAll().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/api/clients/current/account/transaction")
    public Set<AccountDTO> getAuthenticatedClientAccounts(Authentication authentication){
        Client client = clientService.getByEmail(authentication.getName());
        ClientDTO clientDTO = new ClientDTO(client);
        return clientDTO.getAccounts();
    }

    @PostMapping(path = "/api/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication, AccountType accountType){
        Set<Account> clientAccounts = clientService.getByEmail(authentication.getName()).getAccounts();
        Set<Account>  clientValidAccounts = clientAccounts.stream().filter(account -> !account.isDeleted()).collect(Collectors.toSet());
        if(clientValidAccounts.size() >= 3){
            return new ResponseEntity<>("You have 3 accounts already", HttpStatus.FORBIDDEN);
        }
        Account account = new Account(getUniqueNumber(), LocalDateTime.now(), 0, accountType, false);
        clientService.getByEmail(authentication.getName()).addAccount(account);
        accountService.saveAccount(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping(path = "/api/clients/current/accounts/delete")
    public ResponseEntity<Object> deleteAccount(Authentication authentication, @RequestParam String accountNumber){

        if(accountService.getByNumber(accountNumber) == null){
            return new ResponseEntity<>("Account was not found", HttpStatus.NOT_FOUND);
        }
        if(accountService.getByNumber(accountNumber).getClient() != clientService.getByEmail(authentication.getName())){
            return new ResponseEntity<>("Account is not client's property", HttpStatus.FORBIDDEN);
        }
        if(clientService.getByEmail(authentication.getName()).getAccounts().size() < 2){
            return new ResponseEntity<>("Client should have at least one account", HttpStatus.FORBIDDEN);
        }
        if(accountService.getByNumber(accountNumber).getBalance() != 0){
            return new ResponseEntity<>("Make sure to transfer the balance to another account before delete it", HttpStatus.FORBIDDEN);
        }

        accountService.getByNumber(accountNumber).setDeleted(true);

        accountService.saveAccount(accountService.getByNumber(accountNumber));

        return new ResponseEntity<>("Account successfully deleted!", HttpStatus.ACCEPTED);
    }
}
