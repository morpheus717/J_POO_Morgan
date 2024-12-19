package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;
import org.poo.main.user.Exchange;

import java.util.ArrayList;

@Getter @Setter
public class PayOnline extends ExchangeCommand {
    private String cardNumber;
    private double amount;
    private String currency;
    private String description;
    private String commerciant;
    private String email;
    private ArrayList<Exchange> rates;
    private double previousBalance;
    
    public PayOnline(ArrayList<Exchange> rates, ArrayList<Client> clients, String cardNumber,
                     double amount, String currency, String description, String commerciant,
                     String email, int timestamp) {
        super(rates, clients, timestamp);
        this.cardNumber = cardNumber;
        this.rates = rates;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.commerciant = commerciant;
        this.email = email;
    }

    public ObjectNode accept(Visitor visitor) {
        return visitor.visitPayOnline(this);
    }
}
