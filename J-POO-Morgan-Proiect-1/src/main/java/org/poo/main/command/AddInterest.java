package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;

import java.util.ArrayList;

@Getter
@Setter
public final class AddInterest extends Command {
    private String account;

    public AddInterest(final ArrayList<Client> clients, final String account,
                       final int timestamp) {
        super(clients, timestamp);
        this.account = account;
    }

    /**
     * Accepts a visitor to perform the 'AddInterest' operation.
     *
     * @param visitor The visitor that will handle this command.
     * @return The result of the visitor's operation, typically an ObjectNode.
     */
    public ObjectNode accept(final Visitor visitor) {
        return visitor.visitAddInterest(this);
    }
}
