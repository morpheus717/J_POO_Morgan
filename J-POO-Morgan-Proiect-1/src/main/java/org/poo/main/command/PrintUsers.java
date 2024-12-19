package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;

import java.util.ArrayList;

public final class PrintUsers extends Command {

    public PrintUsers(final ArrayList<Client> clients, final int timestamp) {
        super(clients, timestamp);
    }

    /**
     * Accept method for the visitor pattern.
     * This method allows the visitor to process the PrintUsers command.
     *
     * @param visitor The visitor that processes the PrintUsers command.
     * @return A result after processing the PrintUsers command.
     */
    @Override
    public ObjectNode accept(final Visitor visitor) {
        return visitor.visitPrintUsers(this);
    }
}
