package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;

import java.util.ArrayList;

@Getter @Setter
public class AddFunds extends Command {
    private String account;
    private double amount;

    public AddFunds(ArrayList<Client> clients, String account, double amount, int timestamp) {
        super(clients, timestamp);
        this.account = account;
        this.amount = amount;
    }

    public ObjectNode accept(Visitor visitor) {
        visitor.visitAddFunds(this);
        return null;
    }
}
