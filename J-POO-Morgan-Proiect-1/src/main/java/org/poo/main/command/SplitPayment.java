package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;
import org.poo.main.user.Exchange;

import java.util.ArrayList;

@Getter @Setter
public class SplitPayment extends ExchangeCommand {
    private ArrayList<String> accounts;
    private double amount;
    private String currency;
    private boolean success;

    public SplitPayment(ArrayList<Client> clients, ArrayList<Exchange> rates ,
                        ArrayList<String> accounts, double amount, String currency,
                        int timestamp) {
        super(rates, clients, timestamp);
        this.accounts = accounts;
        this.amount = amount;
        this.currency = currency;
        this.success = false;
    }

    public ObjectNode accept(Visitor visitor) {
        visitor.visitSplitPayment(this);
        return null;
    }
}
