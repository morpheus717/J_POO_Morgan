package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;

import java.util.ArrayList;

public class PrintTransactions extends Command {
    @Getter @Setter
    private String email;

    public PrintTransactions(final ArrayList<Client> clients, final String email,
                             final int timestamp) {
        super(clients, timestamp);
        this.email = email;
    }

    /**
     * Accepts a visitor that processes this PrintTransactions command.
     *
     * @param visitor The visitor to process this command.
     * @return An ObjectNode result from the visitor's operation.
     */
    public ObjectNode accept(final Visitor visitor) {
        return visitor.visitPrintTransactions(this);
    }
}
