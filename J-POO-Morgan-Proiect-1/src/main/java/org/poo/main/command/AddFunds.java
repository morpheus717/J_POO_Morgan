package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;

import java.util.ArrayList;

@Getter
@Setter
public class AddFunds extends Command {

    private String account;
    private double amount;

    public AddFunds(final ArrayList<Client> clients, final String account, final double amount,
                    final int timestamp) {
        super(clients, timestamp);
        this.account = account;
        this.amount = amount;
    }

    /**
     * Accepts a visitor that processes this AddFunds command.
     *
     * @param visitor The visitor to process this command.
     * @return An ObjectNode result from the visitor's operation.
     */
    public ObjectNode accept(final Visitor visitor) {
        visitor.visitAddFunds(this);
        return null;
    }
}
