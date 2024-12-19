package org.poo.main.user.transactions;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class FailedSplitPayment extends SplitPaymentTransaction {
    private String error;

    public FailedSplitPayment(final String error, final String description, final int timestamp,
                              final String currency, final double amount,
                              final ArrayList<String> involvedAccounts) {
        super(description, timestamp, currency, amount, involvedAccounts);
        this.error = error;
    }
}
