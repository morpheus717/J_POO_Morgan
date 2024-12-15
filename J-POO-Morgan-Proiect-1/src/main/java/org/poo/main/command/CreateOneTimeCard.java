package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;

import java.util.ArrayList;

public class CreateOneTimeCard extends CreateCard {

    public CreateOneTimeCard(ArrayList<Client> clients, String account, String email,
                             int timestamp) {
        super(clients, account, email, timestamp);
    }

    public ObjectNode accept(Visitor visitor) {
        visitor.visitCreateOneTimeCard(this);
        return null;
    }
}
