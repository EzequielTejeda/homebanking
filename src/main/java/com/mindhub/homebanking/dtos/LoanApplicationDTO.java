package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {

    private Long id;
    private double amount;
    private int payments;
    private String destinationAccount;

    public LoanApplicationDTO() {
    }

    public LoanApplicationDTO(Long id, double amount, int payments, String destinationAccount) {
        this.id = id;
        this.amount = amount;
        this.payments = payments;
        this.destinationAccount = destinationAccount;
    }

    public Long getId() {
        return id;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public int getPayments() {
        return payments;
    }
    public void setPayments(int payments) {
        this.payments = payments;
    }
    public String getDestinationAccount() {
        return destinationAccount;
    }
    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }
}
