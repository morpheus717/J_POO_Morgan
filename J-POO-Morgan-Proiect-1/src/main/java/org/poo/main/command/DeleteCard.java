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
    private String account;

    public DeleteCard(final ArrayList<Client> clients, final String cardNumber,
                      final String email, final int timestamp) {
        super(clients, timestamp);
        this.cardNumber = cardNumber;
        this.email = email;
    }

    /**
     * Accept method for the visitor pattern.
     * This method allows the visitor to process the DeleteCard command.
     *
     * @param visitor The visitor that processes the DeleteCard command.
     * @return A result after processing the DeleteCard command (can be null).
     */
    public ObjectNode accept(final Visitor visitor) {
        visitor.visitDeleteCard(this);
        return null;
    }
}
