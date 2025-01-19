package org.poo.main.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.command.management.CheckCardStatus;
import org.poo.main.command.Command;
import org.poo.main.command.payment.PayOnline;
import org.poo.main.user.transactions.CardPaymentTransaction;
import org.poo.main.Bank;
import org.poo.main.user.transactions.DeleteCardTransaction;
import org.poo.main.user.transactions.Transaction;

/**
 * Represents a payment card with a card number and status, capable of processing payments.
 */
@Getter
@Setter
public class Card {
    private String cardNumber;
    private String status;
    @JsonIgnore
    private Account account;

    /**
     * Constructs a Card.
     *
     * @param cardNumber the card number
     * @param status     the status of the card
     */
    public Card(final String cardNumber, final String status, final Account account) {
        this.cardNumber = cardNumber;
        this.status = status;
        this.account = account;
    }

    public void checkCardStatus(CheckCardStatus command) {
        double absoluteValue = Math.abs(
                this.account.getBalance()
                        - this.account.getMinBalance());

        if (account.getMinBalance() >= account.getBalance()) {
            this.setStatus("frozen");
            account.getTransactions().add(new Transaction(command.getTimestamp(),
                    "You have reached the minimum amount of funds, the card will be frozen"));
        } else if (absoluteValue <= Account.WARNING_THRESHOLD) {
            account.getTransactions().add(new Transaction(command.getTimestamp(),
                    "Warning, reaching minimum amount of funds"));
        }
    }

    public void destroy(Command command) {
        account.getCards().remove(this);
        account.getTransactions().add(new DeleteCardTransaction(cardNumber,
                account.getOwner().getEmail(), account.getIban(), command.getTimestamp()));
    }


    /**
     * Processes a payment using the card.
     * <p>
     * Ensures the account has sufficient balance, optionally converts currency,
     * and records the transaction.
     *
     * @param command the payment command containing payment details
     */
    public boolean pay(final PayOnline command) {
        double cardAmount = command.getAmount() *
                Bank.getExchangeRate(command.getCurrency(), account.getCurrency());
        if (this.status.equals("frozen")) {
            account.getTransactions().add(new Transaction(command.getTimestamp(),
                    "The card is frozen"));
            return false;
        }

        if (account.getBalance() < cardAmount) {
            account.getTransactions().add(new Transaction(command.getTimestamp(),
                    "Insufficient funds"));
            return false;
        }

        account.withdraw(cardAmount);
        account.getTransactions().add(new CardPaymentTransaction(cardAmount,
                command.getCommerciant(), "Card payment", command.getTimestamp()));
        return true;
    }

    public ObjectNode toJSON() {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("cardNumber", this.cardNumber);
        objectNode.put("status", this.status);
        return objectNode;
    }
}
