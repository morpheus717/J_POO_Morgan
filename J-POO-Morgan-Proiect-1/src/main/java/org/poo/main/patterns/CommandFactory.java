package org.poo.main.patterns;

import org.poo.fileio.CommandInput;
import org.poo.main.command.*;
import org.poo.main.command.display.PrintTransactions;
import org.poo.main.command.display.PrintUsers;
import org.poo.main.command.display.Report;
import org.poo.main.command.display.SpendingsReport;
import org.poo.main.command.management.*;
import org.poo.main.command.payment.PayOnline;
import org.poo.main.command.payment.SendMoney;
import org.poo.main.command.payment.SplitPayment;
import org.poo.main.user.Client;
import org.poo.utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class CommandFactory {
    public static Command createCommand(
            CommandInput input, ArrayList<Client> clients,
            HashMap<Pair<String, String>, Double> exchanges) {
        return switch (input.getCommand()) {
            case "printUsers" -> new PrintUsers(clients, input.getTimestamp());
            case "printTransactions" -> new PrintTransactions(
                    input.getEmail(), input.getTimestamp());
            case "addAccount" -> new AddAccount(input.getEmail(),
                    input.getCurrency(), input.getAccountType(),
                    input.getInterestRate(),
                    input.getTimestamp());
            case "addFunds" -> new AddFunds(input.getAccount(),
                    input.getAmount(), input.getTimestamp());
            case "createCard" -> new CreateCard(input.getAccount(),
                    input.getEmail(), input.getTimestamp());
            case "createOneTimeCard" ->
                    new CreateOneTimeCard(input.getAccount(),
                            input.getEmail(), input.getTimestamp());
            case "deleteAccount" -> new DeleteAccount(input.getAccount(),
                    input.getEmail(), input.getTimestamp());
            case "deleteCard" -> new DeleteCard(input.getCardNumber(),
                    input.getEmail(), input.getTimestamp());
            case "setMinimumBalance" -> new SetMinBalance(input.getAmount(),
                    input.getAccount(), input.getTimestamp());
            case "payOnline" -> new PayOnline(input.getCardNumber(),
                    input.getAmount(), input.getCurrency(),
                    input.getDescription(), input.getCommerciant(),
                    input.getEmail(), input.getTimestamp());
            case "sendMoney" -> new SendMoney(input.getAccount(),
                    input.getAmount(), input.getReceiver(),
                    input.getDescription(), input.getEmail(),
                    input.getTimestamp());
            case "setAlias" -> new SetAlias(input.getAlias(),
                    input.getEmail(), input.getAccount(),
                    input.getTimestamp());
            case "checkCardStatus" -> new CheckCardStatus(input.getCardNumber(),
                    input.getTimestamp());
            case "changeInterestRate" -> new ChangeInterestRate(
                    input.getAccount(), input.getInterestRate(),
                    input.getTimestamp());
            case "splitPayment" -> new SplitPayment(
                    (ArrayList<String>) input.getAccounts(),
                    input.getAmount(), input.getCurrency(),
                    input.getTimestamp());
            case "report" -> new Report(input.getAccount(),
                    input.getStartTimestamp(), input.getEndTimestamp(),
                    input.getTimestamp());
            case "spendingsReport" -> new SpendingsReport(input.getAccount(),
                    input.getStartTimestamp(), input.getEndTimestamp(),
                    input.getTimestamp());
            case "addInterest" -> new AddInterest(input.getAccount(),
                    input.getTimestamp());
            default -> null;
        };
    }
}
