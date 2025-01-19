package org.poo.main.patterns;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.command.Command;

public class CommandStrategy {
    private final Command context;

    public CommandStrategy(Command context) {
        this.context = context;
    }

    public void applyCommand(ArrayNode output) {
        ObjectNode out = context.execute();
        if (out != null) {
            output.add(out);
        }
    }
}
