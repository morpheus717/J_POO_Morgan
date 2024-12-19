package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;

import java.util.ArrayList;

@Getter
@Setter
public final class DeleteAccount extends Command {
    private String accountName;
    private String email;

    public DeleteAccount(final ArrayList<Client> clients, final String accountName,
                         final String email, final int timestamp) {
        super(clients, timestamp);
        this.accountName = accountName;
        this.email = email;
    }

    /**
     * Accept method for the visitor pattern.
     * This method allows the visitor to process the DeleteAccount command.
     *
     * @param visitor The visitor that processes the DeleteAccount command.
     * @return A result after processing the DeleteAccount command.
     */
    @Override
    public ObjectNode accept(final Visitor visitor) {
        return visitor.visitDeleteAccount(this);
    }
}
