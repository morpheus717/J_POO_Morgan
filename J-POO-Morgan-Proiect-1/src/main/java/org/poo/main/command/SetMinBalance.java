package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;

import java.util.ArrayList;

@Getter @Setter
public class SetMinBalance extends Command {
    private double amount;
    private String accountName;

    public SetMinBalance(ArrayList<Client> clients, double amount, String account, int timestamp) {
        super(clients, timestamp);
        this.amount = amount;
        this.accountName = account;
    }

    public ObjectNode accept(Visitor visitor) {
        visitor.visitSetMinBalance(this);
        return null;
    }
}
