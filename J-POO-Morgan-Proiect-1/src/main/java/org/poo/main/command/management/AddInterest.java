package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.Bank;
import org.poo.main.user.Account;
import org.poo.main.user.Client;

import java.util.ArrayList;

@Getter
@Setter
public final class AddInterest extends Command {
    private String account;

    public AddInterest(final String account,
                       final int timestamp) {
        super(timestamp);
        this.account = account;
    }

    @Override
    public ObjectNode execute() {
        Account acc = Bank.findByIban(account);
        if (acc != null) {
            return acc.addInterest(this);
        }
        return null;
    }
}
