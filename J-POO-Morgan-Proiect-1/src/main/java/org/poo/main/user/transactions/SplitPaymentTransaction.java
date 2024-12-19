package org.poo.main.user.transactions;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class SplitPaymentTransaction extends Transaction {
    private String currency;
    private double amount;
    private ArrayList<String> involvedAccounts;

    public SplitPaymentTransaction(final String description, final int timestamp,
                                   final String currency, final double amount,
                                   final ArrayList<String> involvedAccounts) {
        super(timestamp, description);
        this.currency = currency;
        this.amount = amount;
        this.involvedAccounts = involvedAccounts;
    }
}
