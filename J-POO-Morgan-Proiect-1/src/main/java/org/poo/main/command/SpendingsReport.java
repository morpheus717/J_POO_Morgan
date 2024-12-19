package org.poo.main.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;

import java.util.ArrayList;

public class SpendingsReport extends Report {

    public SpendingsReport(String account, int startTimestamp, int endTimestamp,
                           ArrayList<Client> clients, int timestamp) {
        super(account, startTimestamp, endTimestamp, clients, timestamp);
    }

    public ObjectNode accept(Visitor visitor) {
        return visitor.visitSpendingsReport(this);
    }

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
