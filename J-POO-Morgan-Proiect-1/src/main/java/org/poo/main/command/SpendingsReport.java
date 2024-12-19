package org.poo.main.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;

import java.util.ArrayList;

public final class SpendingsReport extends Report {

    public SpendingsReport(final String account, final int startTimestamp,
                           final int endTimestamp, final ArrayList<Client> clients,
                           final int timestamp) {
        super(account, startTimestamp, endTimestamp, clients, timestamp);
    }

    /**
     * Accepts a visitor to perform the 'SpendingsReport' operation.
     *
     * @param visitor The visitor that will handle this command.
     * @return The result of the visitor's operation, typically an ObjectNode.
     */
    @Override
    public ObjectNode accept(final Visitor visitor) {
        return visitor.visitSpendingsReport(this);
    }

    /**
     * Generates a report indicating that the account was not found.
     *
     * @return An ObjectNode with the response indicating that the account was not found.
     */
    @Override
    public ObjectNode accountNotFound() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode out = mapper.createObjectNode();
        ObjectNode out2 = mapper.createObjectNode();

        out2.put("description", "Account not found");
        out2.put("timestamp", this.getTimestamp());
        out.put("command", "spendingsReport");
        out.put("output", out2);
        out.put("timestamp", this.getTimestamp());
        return out;
    }
}
