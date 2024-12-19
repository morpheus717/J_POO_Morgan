package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;

import java.util.ArrayList;

@Getter
@Setter
public class SetAlias extends Command {
    private String alias;
    private String email;
    private String accIban;


    public SetAlias(final ArrayList<Client> clients, final String alias, final String email,
                    final String accIban, final int timestamp) {
        super(clients, timestamp);
        this.alias = alias;
        this.email = email;
        this.accIban = accIban;
    }

    /**
     * Accepts a visitor that processes this SetAlias command.
     *
     * @param visitor The visitor to process this command.
     * @return An ObjectNode result from the visitor's operation.
     */
    public ObjectNode accept(final Visitor visitor) {
        visitor.visitSetAlias(this);
        return null;
    }
}
