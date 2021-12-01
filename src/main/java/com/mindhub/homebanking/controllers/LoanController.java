package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class LoanController {

    @Autowired
    private LoanService loanService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientLoanRepository clientLoanRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/api/loans")
    public List<LoanDTO> getLoans(){
        return loanService.getAll().stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @Transactional
    @PostMapping(path = "api/clients/current/loans")
    public ResponseEntity<String> getNewLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO){

        String clientEmail = accountService.getByNumber(loanApplicationDTO.getDestinationAccount()).getClient().getEmail();
        if(!authentication.getName().equals(clientEmail)){
            return new ResponseEntity<>("Destiny account is not client´s property", HttpStatus.FORBIDDEN);
        }

        if(accountService.getByNumber(loanApplicationDTO.getDestinationAccount()).isDeleted()){
            return new ResponseEntity<>("Destiny account is no longer available", HttpStatus.FORBIDDEN);
        }

        Set<Long> currentLoans = clientService.getByEmail(authentication.getName()).getLoans().stream().map(Loan::getId).collect(Collectors.toSet());
        if(currentLoans.contains(loanApplicationDTO.getId())){
            return new ResponseEntity<>("Client have applied for the chosen loan already", HttpStatus.FORBIDDEN);
        }

        Loan chosenLoan = loanService.getById(loanApplicationDTO.getId()).orElse(null);
        if(!chosenLoan.getPayments().contains(loanApplicationDTO.getPayments())){
            return new ResponseEntity<>("Choose an available payment plan", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.getAmount() < 0 || chosenLoan.getMaxAmount() < loanApplicationDTO.getAmount()){
            return new ResponseEntity<>("Invalid amount", HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount() + (loanApplicationDTO.getAmount() * (loanService.getById(loanApplicationDTO.getId()).orElse(null).getInterest())) / 100 , loanApplicationDTO.getPayments());
        Transaction transaction = new Transaction(TransactionType.CREDIT, loanService.getById(loanApplicationDTO.getId()).orElse(null).getName() + " loan approved", loanApplicationDTO.getAmount(), LocalDateTime.now());

        clientService.getByEmail(authentication.getName()).addClientLoan(clientLoan);
        loanService.getById(loanApplicationDTO.getId()).orElse(null).addClientLoan(clientLoan);
        accountService.getByNumber(loanApplicationDTO.getDestinationAccount()).addTransaction(transaction);

        accountService.getByNumber(loanApplicationDTO.getDestinationAccount()).setBalance(accountService.getByNumber(loanApplicationDTO.getDestinationAccount()).getBalance() + loanApplicationDTO.getAmount());

        clientLoanRepository.save(clientLoan);
        transactionRepository.save(transaction);

        return new ResponseEntity<>("Your loan apply was accepted!", HttpStatus.CREATED);
    }
}
