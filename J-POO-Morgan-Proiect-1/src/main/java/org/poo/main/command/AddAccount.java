package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;

import java.util.ArrayList;

@Getter @Setter
public class AddAccount extends Command {
    private String email;
    private String currency;
    private String accountType;
    private double interestRate;
    private String generatedIban;

    public AddAccount(ArrayList<Client> clients, String email, String currency, String accountType,
                      double interestRate, int timestamp) {
        super(clients, timestamp);
        this.email = email;
        this.currency = currency;
        this.accountType = accountType;
        this.interestRate = interestRate;
    }

    @Override
    public ObjectNode accept(Visitor visitor) {
        visitor.visitAddAccount(this);
        return null;
    }
}
