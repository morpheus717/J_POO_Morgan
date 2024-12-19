package org.poo.main.user;

import lombok.Getter;
import lombok.Setter;
import org.poo.main.command.PayOnline;
import org.poo.main.user.transactions.CardPaymentTransaction;

/**
 * Represents a payment card with a card number and status, capable of processing payments.
 */
@Getter
@Setter
public class Card {
    private String cardNumber;
    private String status;

    /**
     * Constructs a Card.
     *
     * @param cardNumber the card number
     * @param status     the status of the card
     */
    public Card(final String cardNumber, final String status) {
        this.cardNumber = cardNumber;
        this.status = status;
    }

    /**
     * Processes a payment using the card.
     * <p>
     * Ensures the account has sufficient balance, optionally converts currency,
     * and records the transaction.
     *
     * @param myAcc   the account to be charged
     * @param command the payment command containing payment details
     */
    public void pay(final Account myAcc, final PayOnline command) {
        if (myAcc.getCurrency().equals(command.getCurrency())) {
            if (myAcc.getBalance() < command.getAmount()) {
                return;
            }
            myAcc.setBalance(myAcc.getBalance() - command.getAmount());
            CardPaymentTransaction transaction = new CardPaymentTransaction(
                    command.getAmount(),
                    command.getCommerciant(),
                    "Card payment",
                    command.getTimestamp()
            );
            myAcc.getTransactions().add(transaction);
            return;
        }

        double amount = command.exchangeMoney(command.getCurrency(), myAcc.getCurrency(),
                command.getAmount());
        if (myAcc.getBalance() < amount) {
            return;
        }

        CardPaymentTransaction transaction = new CardPaymentTransaction(
                amount,
                command.getCommerciant(),
                "Card payment",
                command.getTimestamp()
        );
        myAcc.getTransactions().add(transaction);
        myAcc.setBalance(myAcc.getBalance() - amount);
    }
}
