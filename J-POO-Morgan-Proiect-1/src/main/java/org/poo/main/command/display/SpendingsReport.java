package org.poo.main.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.Bank;
import org.poo.main.patterns.AccountVisitor;
import org.poo.main.patterns.SpendingsReportVisitor;
import org.poo.main.user.Account;
import org.poo.main.user.Client;

import java.util.ArrayList;

public final class SpendingsReport extends Report {

    public SpendingsReport(final String account, final int startTimestamp,
                           final int endTimestamp, final int timestamp) {
        super(account, startTimestamp, endTimestamp, timestamp);
    }

    @Override
    public ObjectNode execute() {
        Account acc = Bank.findByIban(super.getAccount());
        if (acc == null) {
            return accountNotFound();
        }
        return acc.accept(new SpendingsReportVisitor(),
                super.getTimestamp(),
                super.getStartTimestamp(),
                super.getEndTimestamp());
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
