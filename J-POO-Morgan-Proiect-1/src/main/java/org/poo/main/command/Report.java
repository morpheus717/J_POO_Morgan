package org.poo.main.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;

import java.util.ArrayList;

@Getter @Setter
public class Report extends Command {
    private String account;
    private int startTimestamp;
    private int endTimestamp;

    public Report(final String account, final int startTimestamp, final int endTimestamp,
                  final ArrayList<Client> clients, final int timestamp) {
        super(clients, timestamp);
        this.account = account;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
    }

    /**
     * Accept method for the visitor pattern.
     *
     * @param visitor The visitor that processes the report.
     * @return A response from the visitor processing the report.
     */
    public ObjectNode accept(final Visitor visitor) {
        return visitor.visitReport(this);
    }

    /**
     * Generates a response indicating that the account was not found.
     *
     * @return A JSON object indicating the account was not found, with relevant details.
     */
    public ObjectNode accountNotFound() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode out = mapper.createObjectNode();
        ObjectNode out2 = mapper.createObjectNode();

        out2.put("description", "Account not found");
        out2.put("timestamp", this.getTimestamp());
        out.put("command", "report");
        out.put("output", out2);
        out.put("timestamp", this.getTimestamp());
        return out;
    }
}
