package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;

import java.util.ArrayList;

@Getter @Setter
public class DeleteAccount extends Command {
    private String accountName;
    private String email;

    public DeleteAccount(ArrayList<Client> clients, String accountName, String email, int timestamp) {
        super(clients, timestamp);
        this.accountName = accountName;
        this.email = email;
    }

    public ObjectNode accept(Visitor visitor) {
        return visitor.visitDeleteAccount(this);
    }
}
