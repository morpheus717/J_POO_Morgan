package org.poo.main.patterns;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.user.Account;

public class ReportVisitor implements AccountVisitor {

    @Override
    public ObjectNode visit(Account account, int timestamp,
                            int startTimestamp, int endTimestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode out = mapper.createObjectNode();
        out.put("command", "report");
        ObjectNode data = mapper.createObjectNode();
        data.put("IBAN", account.getIban());
        data.put("balance", account.getBalance());
        data.put("currency", account.getCurrency());
        data.put("transactions", account.transactionsToJson(startTimestamp, endTimestamp));
        out.put("output", data);
        out.put("timestamp", timestamp);
        return out;
    }
}
