package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;

import java.util.ArrayList;

@Getter
@Setter
public class CreateCard extends Command {
    private String account;
    private String email;
    private String generatedNumber;

    public CreateCard(final ArrayList<Client> clients, final String account,
                      final String email, final int timestamp) {
        super(clients, timestamp);
        this.account = account;
        this.email = email;
    }

    /**
     * Accepts a visitor to perform the 'CreateCard' operation.
     *
     * @param visitor The visitor that will handle this command.
     * @return The result of the visitor's operation, typically an ObjectNode.
     */
    @Override
    public ObjectNode accept(final Visitor visitor) {
        visitor.visitCreateCard(this);
        return null;
    }
}
