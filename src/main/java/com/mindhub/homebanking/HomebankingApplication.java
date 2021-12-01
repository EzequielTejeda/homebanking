package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository,
									  TransactionRepository transactionRepository, LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository, CardRepository cardRepository){
		return (args) -> {
				Client client1=new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("MorelM86"));
			Client client2=new Client("Olivia", "Ortega", "oli97@gmail.com", passwordEncoder.encode("PopeyeXx"));
			Client client3=new Client("Ezequiel", "Tejeda", "eze23-96@hotmail.com", passwordEncoder.encode("puflito"));

			Account account1 = new Account("VIN001", LocalDateTime.now(), 5000, AccountType.CURRENT, false);
			Account account2 = new Account("VIN002", LocalDateTime.now().plusDays(1), 7500, AccountType.SAVINGS, false);
			Account account3 = new Account("VIN003", LocalDateTime.now(), 4500, AccountType.CURRENT, false);
			Account account4 = new Account("VIN004", LocalDateTime.now().plusDays(1), 8000, AccountType.SAVINGS, false);

			Transaction transaction1 = new Transaction(TransactionType.CREDIT, "HSBC check", 1500, LocalDateTime.now());
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, "EPEC bill", -300, LocalDateTime.now());
			Transaction transaction3 = new Transaction(TransactionType.CREDIT, "Fees", 900, LocalDateTime.now());
			Transaction transaction4 = new Transaction(TransactionType.DEBIT, "Personal bill", -450, LocalDateTime.now());
			Transaction transaction5 = new Transaction(TransactionType.CREDIT, "Raul Castañera transfer", 800, LocalDateTime.now());
			Transaction transaction6 = new Transaction(TransactionType.DEBIT, "Leash Franco-PetShop", -150, LocalDateTime.now());
			Transaction transaction7 = new Transaction(TransactionType.CREDIT, "S.O.S S.A deposit", 3000, LocalDateTime.now());
			Transaction transaction8 = new Transaction(TransactionType.DEBIT, "Tarjeta Naranja abstract", -1800, LocalDateTime.now());

			List<Integer> payment1 = List.of(12,24,36,48,60);
			Loan loan1 = new Loan("Mortgage", 500000, payment1, 30);
			List<Integer> payment2 = List.of(6,12,24);
			Loan loan2 = new Loan("Personal", 100000, payment2, 20);
			List<Integer> payment3 = List.of(6,12,24,36);
			Loan loan3 = new Loan("Vehicle", 300000, payment3, 25);

			ClientLoan clientLoan1 = new ClientLoan(400000, 60);
			ClientLoan clientLoan2 = new ClientLoan(50000, 12);
			ClientLoan clientLoan3 = new ClientLoan(100000, 24);
			ClientLoan clientLoan4 = new ClientLoan(200000, 36);

			Card card1 = new Card(client1.getLastName()+" "+client1.getFirstName(), CardType.DEBIT, CardColor.GOLD, "4029 5678 9876 1234", 789, LocalDateTime.now(), LocalDateTime.now().plusYears(5), false, false);
			Card card2 = new Card(client1.getLastName()+" "+client1.getFirstName(), CardType.CREDIT, CardColor.PLATINUM, "5001 1234 5678 9876", 321, LocalDateTime.now(), LocalDateTime.now().plusYears(5), false, false);
			Card card3 = new Card(client2.getLastName()+" "+client2.getFirstName(), CardType.CREDIT, CardColor.SILVER, "5001 8567 3529 0582", 739, LocalDateTime.now().minusMonths(1), LocalDateTime.now().minusMonths(1).plusYears(5), false, false);

			client1.addCard(card1);
			client1.addCard(card2);
			client2.addCard(card3);

			client1.addClientLoan(clientLoan1);
			client1.addClientLoan(clientLoan2);
			client2.addClientLoan(clientLoan3);
			client2.addClientLoan(clientLoan4);
			loan1.addClientLoan(clientLoan1);
			loan2.addClientLoan(clientLoan2);
			loan2.addClientLoan(clientLoan3);
			loan3.addClientLoan(clientLoan4);

			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account2.addTransaction(transaction3);
			account2.addTransaction(transaction4);
			account3.addTransaction(transaction5);
			account3.addTransaction(transaction6);
			account4.addTransaction(transaction7);
			account4.addTransaction(transaction8);

			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);
			client2.addAccount(account4);

			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(client3);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);
			transactionRepository.save(transaction7);
			transactionRepository.save(transaction8);

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);
		};
	}
}
