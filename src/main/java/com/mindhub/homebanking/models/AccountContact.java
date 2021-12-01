package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class AccountContact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private ContactType contactType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contact_id")
    private Contact contact;

    public AccountContact() {
    }

    public AccountContact(ContactType contactType) {
        this.contactType = contactType;
    }

    public Long getId() {
        return id;
    }
    public ContactType getContactType() {
        return contactType;
    }
    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }
    public Account getAccount() {
        return account;
    }
    public void setAccount(Account account) {
        this.account = account;
    }
    public Contact getContact() {
        return contact;
    }
    public void setContact(Contact contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "AccountContact{" +
                "id=" + id +
                ", contactType='" + contactType + '\'' +
                ", account=" + account +
                ", contact=" + contact +
                '}';
    }
}
