package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;

import java.util.ArrayList;

@Getter
@Setter
public class SetMinBalance extends Command {
    private double amount;
    private String accountName;

    public SetMinBalance(final ArrayList<Client> clients, final double amount,
                         final String account, final int timestamp) {
        super(clients, timestamp);
        this.amount = amount;
        this.accountName = account;
    }

    /**
     * Accepts a visitor to perform operations specific to the SetMinBalance command.
     *
     * @param visitor the visitor performing the operation
     * @return an ObjectNode representing the result of the operation (can be null)
     */
    public ObjectNode accept(final Visitor visitor) {
        visitor.visitSetMinBalance(this);
        return null;
    }
}
