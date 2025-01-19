package org.poo.main.command.management;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.Bank;
import org.poo.main.Main;
import org.poo.main.command.Command;
import org.poo.main.user.Account;
import org.poo.main.user.Client;
import org.poo.main.user.transactions.Transaction;

@Getter
@Setter
public final class DeleteAccount extends Command {
    private String accountName;
    private String email;

    public DeleteAccount(final String accountName,
                         final String email, final int timestamp) {
        super(timestamp);
        this.accountName = accountName;
        this.email = email;
    }

    @Override
    public ObjectNode execute() {
        Client client = Bank.findByEmail(email);
        if (client == null) {
            return Main.generateOutputEntry("deleteAccount",
                    new Transaction(super.getTimestamp(), "User not found").toJson(),
                    super.getTimestamp());
        }
        Account acc = Bank.findByIban(accountName);
        if (acc != null && acc.getOwner() == client) {
            return Main.generateOutputEntry( "deleteAccount",
                    acc.destroyAccount(this), super.getTimestamp());
        }
        return null;
    }
}
