package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;

import java.util.ArrayList;

public class CheckCardStatus extends Command {
    @Getter
    private String cardNumber;

    public CheckCardStatus(final ArrayList<Client> clients, final String cardNumber,
                           final int timestamp) {
        super(clients, timestamp);
        this.cardNumber = cardNumber;
    }

    /**
     * Accept method for the visitor pattern.
     * This method allows the visitor to process the CheckCardStatus command.
     *
     * @param visitor The visitor that processes the CheckCardStatus command.
     * @return A result after processing the CheckCardStatus command.
     */
    public ObjectNode accept(final Visitor visitor) {
        return visitor.visitCheckCardStatus(this);
    }
}
