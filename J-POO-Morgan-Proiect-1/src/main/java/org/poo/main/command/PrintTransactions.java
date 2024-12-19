package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;

import java.util.ArrayList;

public class PrintTransactions extends Command {
    @Getter @Setter
    private String email;

    public PrintTransactions(ArrayList<Client> clients, String email, int timestamp) {
        super(clients, timestamp);
        this.email = email;
    }

    public ObjectNode accept(Visitor visitor) {
        return visitor.visitPrintTransactions(this);
    }
}
