package org.poo.main.user.transactions;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a card payment transaction, including the amount, merchant, and description.
 */
@Getter
@Setter
public class CardPaymentTransaction extends Transaction {
    private double amount;
    private String commerciant;

    public CardPaymentTransaction(final double amount, final String commerciant,
                                  final String description, final int timestamp) {
        super(timestamp, description);
        this.amount = amount;
        this.commerciant = commerciant;
    }

    /**
     * The purpose of this function is to filter Spendings types of transactions
     * by being overwritten
     * @return always null
     */
    @Override
    public CardPaymentTransaction filterSpendings() {
        return this;
    }
}
