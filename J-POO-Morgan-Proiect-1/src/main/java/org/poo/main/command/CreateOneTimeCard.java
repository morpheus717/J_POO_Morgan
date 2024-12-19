package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;

import java.util.ArrayList;

@Getter
@Setter
public class CreateOneTimeCard extends CreateCard {

    public CreateOneTimeCard(final ArrayList<Client> clients, final String account,
                             final String email, final int timestamp) {
        super(clients, account, email, timestamp);
    }

    /**
     * Accepts a visitor that processes this CreateOneTimeCard command.
     *
     * @param visitor The visitor to process this command.
     * @return An ObjectNode result from the visitor's operation.
     */
    public ObjectNode accept(final Visitor visitor) {
        visitor.visitCreateOneTimeCard(this);
        return null;
    }
}
