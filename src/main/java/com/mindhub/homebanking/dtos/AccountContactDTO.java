package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.AccountContact;
import com.mindhub.homebanking.models.ContactType;

public class AccountContactDTO {

    private Long id;
    private Long contactId;
    private ContactType contactType;
    private String name;
    private String accountNumber;

    public AccountContactDTO(AccountContact accountContact){
        this.id = accountContact.getId();
        this.contactId = accountContact.getContact().getId();
        this.contactType = accountContact.getContactType();
        this.name = accountContact.getContact().getName();
        this.accountNumber = accountContact.getContact().getAccountNumber();
    }

    public Long getId() {
        return id;
    }
    public Long getContactId() {
        return contactId;
    }
    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }
    public ContactType getContactType() {
        return contactType;
    }
    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
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
}
