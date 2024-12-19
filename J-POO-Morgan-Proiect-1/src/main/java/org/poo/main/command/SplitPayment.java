package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;
import org.poo.main.user.Exchange;

import java.util.ArrayList;

@Getter @Setter
public final class SplitPayment extends ExchangeCommand {
    private ArrayList<String> accounts;
    private double amount;
    private String currency;
    private boolean success;

    public SplitPayment(final ArrayList<Client> clients, final ArrayList<Exchange> rates,
                        final ArrayList<String> accounts, final double amount,
                        final String currency, final int timestamp) {
        super(rates, clients, timestamp);
        this.accounts = accounts;
        this.amount = amount;
        this.currency = currency;
        this.success = false;
    }

    /**
     * Accepts a visitor to perform the 'SplitPayment' operation.
     *
     * @param visitor The visitor that will handle this command.
     * @return The result of the visitor's operation, typically an ObjectNode.
     */
    @Override
    public ObjectNode accept(final Visitor visitor) {
        visitor.visitSplitPayment(this);
        return null;
    }
}
