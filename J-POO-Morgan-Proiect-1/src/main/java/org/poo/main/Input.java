package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.*;
import org.poo.main.command.*;
import org.poo.main.patterns.ExecuteVisitor;
import org.poo.main.user.Client;
import org.poo.main.user.Exchange;

import java.util.ArrayList;

public class Input {
    private static ArrayList<Client> clients;
    private static ArrayList<Exchange> exchanges;

    public static Client processUserInput(UserInput userInput) {
        return new Client(userInput.getFirstName(), userInput.getLastName(),
                                      userInput.getEmail());
    }
    public static Exchange processExchangeInput(ExchangeInput exchangeInput) {
        return new Exchange(exchangeInput.getFrom(), exchangeInput.getTo(),
                            exchangeInput.getRate());
    }
    public static Command processCommandInput(CommandInput commandInput) {
        switch (commandInput.getCommand()) {
            case "printUsers":
                return new PrintUsers(clients, commandInput.getTimestamp());
            // case "printTransactions":
               // return new PrintTransactions(commandInput.getEmail(), commandInput.getTimestamp());
            case "addAccount":
                return new AddAccount(clients, commandInput.getEmail(), commandInput.getCurrency(),
                        commandInput.getAccountType(), commandInput.getInterestRate(),
                        commandInput.getTimestamp());
            case "addFunds":
                return new AddFunds(clients, commandInput.getAccount(), commandInput.getAmount(),
                        commandInput.getTimestamp());
            case "createCard":
                return new CreateCard(clients, commandInput.getAccount(), commandInput.getEmail(),
                        commandInput.getTimestamp());
            case "createOneTimeCard":
                return new CreateOneTimeCard(clients, commandInput.getAccount(), commandInput.getEmail(),
                        commandInput.getTimestamp());
            case "deleteAccount":
                return new DeleteAccount(clients, commandInput.getAccount(),
                        commandInput.getEmail(), commandInput.getTimestamp());
            case "deleteCard":
                return new DeleteCard(clients, commandInput.getCardNumber(),
                        commandInput.getEmail(), commandInput.getTimestamp());
            case "setMinBalance":
                return new SetMinBalance(clients, commandInput.getAmount(),
                        commandInput.getAccount(), commandInput.getTimestamp());

        }
        return null;
    }

    public static ArrayNode processInput(ObjectInput inputData) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode arrayNode = mapper.createArrayNode();
        ExecuteVisitor v = new ExecuteVisitor();
        clients = new ArrayList<>();
        exchanges = new ArrayList<>();

        for (UserInput userInput : inputData.getUsers()) {
            clients.add(processUserInput(userInput));
        }
        for (ExchangeInput exchangeInput : inputData.getExchangeRates()) {
            exchanges.add(processExchangeInput(exchangeInput));
        }
        for (CommandInput commandInput : inputData.getCommands()) {
            Command currCommand = processCommandInput(commandInput);
            ObjectNode result = currCommand != null ? currCommand.accept(v) : null;
            if (result != null) {
                arrayNode.add(result);
            }
        }
        return arrayNode;
    }
}
