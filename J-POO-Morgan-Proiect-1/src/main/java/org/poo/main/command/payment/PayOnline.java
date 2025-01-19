package org.poo.main.command.payment;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.Bank;
import org.poo.main.Main;
import org.poo.main.command.Command;
import org.poo.main.user.Card;
import org.poo.main.user.Client;
import org.poo.main.user.transactions.Transaction;

@Getter @Setter
public final class PayOnline extends Command {
    private String cardNumber;
    private double amount;
    private String currency;
    private String description;
    private String commerciant;
    private String email;
    private double previousBalance;

    public PayOnline(final String cardNumber, final double amount, final String currency,
                     final String description, final String commerciant,
                     final String email, final int timestamp) {
        super(timestamp);
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.commerciant = commerciant;
        this.email = email;
    }

    @Override
    public ObjectNode execute() {
        Client client = Bank.findByEmail(email);
        if (client == null) {
            return Main.generateOutputEntry("payOnline",
                    new Transaction(super.getTimestamp(), "User not found").toJson(),
                    super.getTimestamp());
        }
        Card card = Bank.findByNumber(cardNumber);
        if (card == null) {
            return Main.generateOutputEntry("payOnline",
                    new Transaction(super.getTimestamp(), "Card not found").toJson(),
                    super.getTimestamp());
        }
        card.pay(this);
        return null;
    }
}
