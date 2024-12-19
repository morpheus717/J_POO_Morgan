package org.poo.main.user.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Transaction {
    private int timestamp;
    private String description;

    public Transaction(int timestamp, String description) {
        this.timestamp = timestamp;
        this.description = description;
    }

    public CardPaymentTransaction filterSpendings() {
        return null;
    }
}
