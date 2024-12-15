package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;

import java.util.ArrayList;

public class PrintUsers extends Command {

    public PrintUsers(ArrayList<Client> clients, int timestamp) {
        super(clients, timestamp);
    }

    @Override
    public ObjectNode accept(Visitor visitor) {
        return visitor.visitPrintUsers(this);
    }
}
