package org.poo.main.command.display;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.Bank;
import org.poo.main.command.Command;
import org.poo.main.patterns.ReportVisitor;
import org.poo.main.user.Account;

@Getter @Setter
public class Report extends Command {
    private String account;
    private int startTimestamp;
    private int endTimestamp;

    public Report(final String account, final int startTimestamp,
                  final int endTimestamp, final int timestamp) {
        super(timestamp);
        this.account = account;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
    }

    public ObjectNode execute() {
        Account acc = Bank.findByIban(account);
        if (acc == null) {
            return accountNotFound();
        }
        return acc.accept(
                new ReportVisitor(),
                super.getTimestamp(),
                startTimestamp,
                endTimestamp);
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
