package org.poo.main.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
public class Account {
    @JsonProperty("IBAN")
    private String iban;
    private double balance;
    private String currency;
    private String type;
    private ArrayList<Card> cards;
    @JsonIgnore
    private double minBalance;

    public Account(String iban, double balance, String currency, String type) {
        this.iban = iban;
        this.balance = balance;
        this.currency = currency;
        this.type = type;
        this.cards = new ArrayList<>();
        this.minBalance = 0;
    }
}
