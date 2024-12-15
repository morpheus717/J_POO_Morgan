package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;

import java.util.ArrayList;

public class CheckCardStatus extends Command {
    private String cardNumber;

    public CheckCardStatus(ArrayList<Client> clients, String cardNumber, int timestamp) {
        super(clients, timestamp);
        this.cardNumber = cardNumber;
    }

    public ObjectNode accept(Visitor visitor) {
        return visitor.visitCheckCardStatus(this);
    }
}
