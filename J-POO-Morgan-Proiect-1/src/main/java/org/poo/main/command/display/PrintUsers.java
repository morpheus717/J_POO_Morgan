package org.poo.main.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.Main;
import org.poo.main.user.Client;

import java.util.ArrayList;

public final class PrintUsers extends Command {

    private ArrayList<Client> clients;

    public PrintUsers(final ArrayList<Client> clients, final int timestamp) {
        super(timestamp);
        this.clients = clients;
    }

    @Override
    public ObjectNode execute() {
        ArrayNode out = new ObjectMapper().createArrayNode();
        for (Client c : clients) {
            out.add(c.toJSON());
        }
        return Main.generateOutputEntry("printUsers", out, super.getTimestamp());
    }
}
