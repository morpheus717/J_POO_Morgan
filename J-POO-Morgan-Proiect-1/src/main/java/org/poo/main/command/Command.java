package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.user.Client;

import java.util.ArrayList;

@Getter @Setter
public abstract class Command {
    private int timestamp;

    public Command(final int timestamp) {
        this.timestamp = timestamp;
    }

    public abstract ObjectNode execute();
}
