package org.poo.main.user.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Transaction {
    private int timestamp;
    private String description;

    public Transaction(final int timestamp, final String description) {
        this.timestamp = timestamp;
        this.description = description;
    }

    /**
     * The purpose of this method is to filter Spending type transactions by being overriden
     * @return always null
     */
    public CardPaymentTransaction filterSpendings() {
        return null;
    }
}
