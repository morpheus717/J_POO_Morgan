package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.*;
import org.poo.main.command.*;
import org.poo.main.patterns.ExecuteVisitor;
import org.poo.main.patterns.HistoryVisitor;
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
        return switch (commandInput.getCommand()) {
            case "printUsers" -> new PrintUsers(clients, commandInput.getTimestamp());
            case "printTransactions" -> new PrintTransactions(clients,
                    commandInput.getEmail(), commandInput.getTimestamp());
            case "addAccount" -> new AddAccount(clients, commandInput.getEmail(),
                    commandInput.getCurrency(), commandInput.getAccountType(),
                    commandInput.getInterestRate(),
                    commandInput.getTimestamp());
            case "addFunds" -> new AddFunds(clients, commandInput.getAccount(),
                    commandInput.getAmount(), commandInput.getTimestamp());
            case "createCard" -> new CreateCard(clients, commandInput.getAccount(),
                    commandInput.getEmail(), commandInput.getTimestamp());
            case "createOneTimeCard" ->
                    new CreateOneTimeCard(clients, commandInput.getAccount(),
                            commandInput.getEmail(), commandInput.getTimestamp());
            case "deleteAccount" -> new DeleteAccount(clients, commandInput.getAccount(),
                    commandInput.getEmail(), commandInput.getTimestamp());
            case "deleteCard" -> new DeleteCard(clients, commandInput.getCardNumber(),
                    commandInput.getEmail(), commandInput.getTimestamp());
            case "setMinimumBalance" -> new SetMinBalance(clients, commandInput.getAmount(),
                    commandInput.getAccount(), commandInput.getTimestamp());
            case "payOnline" -> new PayOnline(exchanges, clients, commandInput.getCardNumber(),
                    commandInput.getAmount(), commandInput.getCurrency(),
                    commandInput.getDescription(), commandInput.getCommerciant(),
                    commandInput.getEmail(), commandInput.getTimestamp());
            case "sendMoney" -> new SendMoney(exchanges, clients,
                    commandInput.getAccount(),
                    commandInput.getAmount(), commandInput.getReceiver(),
                    commandInput.getDescription(), commandInput.getEmail(),
                    commandInput.getTimestamp());
            case "setAlias" -> new SetAlias(clients, commandInput.getAlias(),
                    commandInput.getEmail(), commandInput.getAccount(),
                    commandInput.getTimestamp());
            case "checkCardStatus" -> new CheckCardStatus(clients, commandInput.getCardNumber(),
                    commandInput.getTimestamp());
            case "changeInterestRate" -> new ChangeInterestRate(clients,
                    commandInput.getAccount(), commandInput.getInterestRate(),
                    commandInput.getTimestamp());
            case "splitPayment" -> new SplitPayment(clients, exchanges,
                    (ArrayList<String>) commandInput.getAccounts(),
                    commandInput.getAmount(), commandInput.getCurrency(),
                    commandInput.getTimestamp());
            case "report" -> new Report(commandInput.getAccount(),
                    commandInput.getStartTimestamp(), commandInput.getEndTimestamp(),
                    clients, commandInput.getTimestamp());
            case "spendingsReport" -> new SpendingsReport(commandInput.getAccount(),
                    commandInput.getStartTimestamp(), commandInput.getEndTimestamp(),
                    clients, commandInput.getTimestamp());
            case "addInterest" -> new AddInterest(clients, commandInput.getAccount(),
                    commandInput.getTimestamp());
            default -> null;
        };
    }

    public static ArrayNode processInput(ObjectInput inputData) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode arrayNode = mapper.createArrayNode();
        ExecuteVisitor v = new ExecuteVisitor();
        HistoryVisitor h = new HistoryVisitor();
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
            ObjectNode historyResult = currCommand != null ? currCommand.accept(h) : null;

            if (result != null) {
                arrayNode.add(result);
            }
            if (historyResult != null) {
                arrayNode.add(historyResult);
            }
        }
        return arrayNode;
    }
}
