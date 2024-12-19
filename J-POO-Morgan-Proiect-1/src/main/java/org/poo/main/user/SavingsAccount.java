package org.poo.main.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class SavingsAccount extends Account {
    public SavingsAccount(String iban, double balance, String currency, String type) {
        super(iban, balance, currency, type);
    }

    public void modifyInterestRate(double interestRate) {
        this.setInterestRate(interestRate);
    }
}
