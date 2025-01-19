package org.poo.main.command.management;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.Bank;
import org.poo.main.Main;
import org.poo.main.command.Command;
import org.poo.main.user.Client;
import org.poo.main.user.transactions.Transaction;

@Getter
@Setter
public class SetAlias extends Command {
    private String alias;
    private String email;
    private String accIban;


    public SetAlias(final String alias, final String email,
                    final String accIban, final int timestamp) {
        super(timestamp);
        this.alias = alias;
        this.email = email;
        this.accIban = accIban;
    }

    @Override
    public ObjectNode execute() {
        Client client = Bank.findByEmail(email);
        if (client == null) {
            return Main.generateOutputEntry("setAlias",
                    new Transaction(super.getTimestamp(), "User not found").toJson(),
                    super.getTimestamp());
        }
        client.getAliases().put(alias, accIban);
        return null;
    }
}
