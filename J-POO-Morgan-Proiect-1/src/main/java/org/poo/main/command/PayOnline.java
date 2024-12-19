package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;
import org.poo.main.user.Exchange;

import java.util.ArrayList;

@Getter @Setter
public final class PayOnline extends ExchangeCommand {
    private String cardNumber;
    private double amount;
    private String currency;
    private String description;
    private String commerciant;
    private String email;
    private ArrayList<Exchange> rates;
    private double previousBalance;

    public PayOnline(final ArrayList<Exchange> rates, final ArrayList<Client> clients,
                     final String cardNumber, final double amount, final String currency,
                     final String description, final String commerciant,
                     final String email, final int timestamp) {
        super(rates, clients, timestamp);
        this.cardNumber = cardNumber;
        this.rates = rates;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.commerciant = commerciant;
        this.email = email;
    }

    /**
     * Accepts a visitor to perform the 'PayOnline' operation.
     *
     * @param visitor The visitor that will handle this command.
     * @return The result of the visitor's operation, typically an ObjectNode.
     */
    @Override
    public ObjectNode accept(final Visitor visitor) {
        return visitor.visitPayOnline(this);
    }
}
