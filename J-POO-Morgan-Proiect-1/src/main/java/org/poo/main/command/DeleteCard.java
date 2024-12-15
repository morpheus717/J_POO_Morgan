package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;

import java.util.ArrayList;

@Getter @Setter
public class DeleteCard extends Command {
    private String cardNumber;
    private String email;

    public DeleteCard(ArrayList<Client> clients, String cardNumber, String email, int timestamp) {
        super(clients, timestamp);
        this.cardNumber = cardNumber;
        this.email = email;
    }

    public ObjectNode accept(Visitor visitor) {
        visitor.visitDeleteCard(this);
        return null;
    }
}
