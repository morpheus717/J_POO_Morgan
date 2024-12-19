package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;

import java.util.ArrayList;

@Getter @Setter
public class SetAlias extends Command {
    private String alias;
    private String email;
    private String accIban;

    public SetAlias(ArrayList<Client> clients, String alias, String email,
                    String accIban, int timestamp) {
        super(clients, timestamp);
        this.alias = alias;
        this.email = email;
        this.accIban = accIban;
    }

    public ObjectNode accept(Visitor visitor) {
        visitor.visitSetAlias(this);
        return null;
    }
}
