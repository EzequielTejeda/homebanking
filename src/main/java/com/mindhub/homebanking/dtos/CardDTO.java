package com.mindhub.homebanking.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;

import java.time.LocalDateTime;

public class CardDTO {
    private Long id;
    private String cardHolder, number;
    private CardType type;
    private CardColor color;
    private int cvv;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/yy")
    private LocalDateTime fromDate, thruDate;
    private boolean expired, deleted;

    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardHolder = card.getCardHolder();
        this.number = card.getNumber();
        this.type = card.getType();
        this.color = card.getColor();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
        this.expired = card.isExpired();
        this.deleted = card.isDeleted();
    }

    public Long getId() {
        return id;
    }
    public String getCardHolder() {
        return cardHolder;
    }
    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public CardType getType() {
        return type;
    }
    public void setType(CardType type) {
        this.type = type;
    }
    public CardColor getColor() {
        return color;
    }
    public void setColor(CardColor color) {
        this.color = color;
    }
    public int getCvv() {
        return cvv;
    }
    public void setCvv(int cvv) {
        this.cvv = cvv;
    }
    public LocalDateTime getFromDate() {
        return fromDate;
    }
    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }
    public LocalDateTime getThruDate() {
        return thruDate;
    }
    public void setThruDate(LocalDateTime thruDate) {
        this.thruDate = thruDate;
    }
    public boolean isExpired() {
        return expired;
    }
    public void setExpired(boolean expired) {
        this.expired = expired;
    }
    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
