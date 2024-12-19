package org.poo.main.user.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CardPaymentTransaction extends Transaction {
    private double amount;
    private String commerciant;

    public CardPaymentTransaction(double amount, String commerciant, String description,
                                  int timestamp) {
        super(timestamp, description);
        this.amount = amount;
        this.commerciant = commerciant;
    }

    @Override
    public CardPaymentTransaction filterSpendings() {
        return this;
    }
}
