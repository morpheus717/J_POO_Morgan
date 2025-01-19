package org.poo.utils;

import org.poo.fileio.CommandInput;
import org.poo.fileio.ExchangeInput;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;
import org.poo.main.command.Command;
import org.poo.main.patterns.CommandFactory;
import org.poo.main.user.Client;

import java.util.ArrayList;
import java.util.HashMap;

public class InputProcessor {

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

    public static void processInput(
            ObjectInput inputData, ArrayList<Client> clients,
            HashMap<Pair<String, String>, Double> exchanges) {
        processUsersInput(inputData.getUsers(), clients);
        processExchangesInput(inputData.getExchangeRates(), exchanges);
    }
}
