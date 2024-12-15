package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;

import java.util.ArrayList;

@Getter @Setter
public class CreateCard extends Command {
    private String account;
    private String email;

    public CreateCard(ArrayList<Client> clients, String account, String email, int timestamp) {
        super(clients, timestamp);
        this.account = account;
        this.email = email;
    }

    @Override
    public ObjectNode accept(Visitor visitor) {
        visitor.visitCreateCard(this);
        return null;
    }
}
