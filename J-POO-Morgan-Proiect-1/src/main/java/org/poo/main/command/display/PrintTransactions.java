package org.poo.main.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.Bank;
import org.poo.main.user.Client;

import java.util.ArrayList;

@Setter
@Getter
public class PrintTransactions extends Command {
    private String email;

    public PrintTransactions(final String email,
                             final int timestamp) {
        super(timestamp);
        this.email = email;
    }

    @Override
    public ObjectNode execute() {
        ObjectNode output = new ObjectMapper().createObjectNode();
        output.put("command", "printTransactions");
        Client client = Bank.findByEmail(email);
        if (client != null) {
            output.put("output", client.transactionsToJson());
            output.put("timestamp", getTimestamp());
            return output;
        }
        return null;
    }
}
