package org.poo.main.command.management;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.Bank;
import org.poo.main.command.Command;
import org.poo.main.user.Account;

@Getter
@Setter
public class SetMinBalance extends Command {
    private double amount;
    private String accountName;

    public SetMinBalance(final double amount,
                         final String account, final int timestamp) {
        super(timestamp);
        this.amount = amount;
        this.accountName = account;
    }

    @Override
    public ObjectNode execute() {
        Account account = Bank.findByIban(accountName);
        if (account != null) {
            account.setMinBalance(amount);
        }
        return null;
    }
}
