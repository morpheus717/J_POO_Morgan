package org.poo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ExchangeInput;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;
import org.poo.main.command.Command;
import org.poo.main.patterns.CommandFactory;
import org.poo.main.patterns.ExecuteVisitor;
import org.poo.main.patterns.HistoryVisitor;
import org.poo.main.user.Client;

import java.util.ArrayList;
import java.util.HashMap;

public class InputProcesser {

    public static void processUsersInput(UserInput[] userInput, ArrayList<Client> clients) {
        for (UserInput input : userInput) {
            clients.add(new Client(input.getFirstName(), input.getLastName(),
                    input.getEmail()));
        }
    }
    public static void processExchangesInput(
            ExchangeInput[] exchangeInput,
            HashMap<Pair<String, String>, Double> exchanges) {

        for (ExchangeInput input : exchangeInput) {
            exchanges.put(new Pair<>(input.getFrom(), input.getTo()), input.getRate());
            exchanges.put(new Pair<>(input.getTo(), input.getFrom()), 1 / input.getRate());
        }
    }

    public static Command processCommandInput(
            CommandInput commandInput, ArrayList<Client> clients,
            HashMap<Pair<String, String>, Double> exchanges) {
        return CommandFactory.createCommand(commandInput, clients, exchanges);
    }

    public static ArrayNode processInput(ObjectInput inputData) {
//        ObjectMapper mapper = new ObjectMapper();
//        ArrayNode arrayNode = mapper.createArrayNode();
//        ExecuteVisitor v = new ExecuteVisitor();
//        HistoryVisitor h = new HistoryVisitor();
//        clients = new ArrayList<>();
//
//        processUsersInput(inputData.getUsers());
//        processExchangesInput(inputData.getExchangeRates());
//
//        for (CommandInput commandInput : inputData.getCommands()) {
//            Command currCommand = processCommandInput(commandInput);
//            ObjectNode result = currCommand != null ? currCommand.accept(v) : null;
//            ObjectNode historyResult = currCommand != null ? currCommand.accept(h) : null;
//
//            if (result != null) {
//                arrayNode.add(result);
//            }
//            if (historyResult != null) {
//                arrayNode.add(historyResult);
//            }
//        }
//        return arrayNode;
//    }
}
