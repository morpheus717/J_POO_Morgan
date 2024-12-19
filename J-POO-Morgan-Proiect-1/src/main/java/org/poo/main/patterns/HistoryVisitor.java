package org.poo.main.patterns;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.command.*;
import org.poo.main.user.Account;
import org.poo.main.user.Card;
import org.poo.main.user.Client;
import org.poo.main.user.transactions.*;

import java.util.ArrayList;

public class HistoryVisitor implements Visitor {

    @Override
    public ObjectNode visitPrintUsers(PrintUsers command) {
        return null;
    }

    @Override
    public void visitAddAccount(AddAccount command) {
        AddAccountTransaction transaction = new AddAccountTransaction(command.getTimestamp());
        Account acc = command.findByIban(command.getGeneratedIban());
        acc.getTransactions().add(transaction);
    }

    @Override
    public void visitCreateCard(CreateCard command) {
        CreateCardTransaction transaction = new CreateCardTransaction(command.getAccount(),
                command.getGeneratedNumber(), command.getEmail(), "New card created",
                command.getTimestamp());
        Account acc = command.findByIban(command.getAccount());
        acc.getTransactions().add(transaction);
    }

    @Override
    public void visitCreateOneTimeCard(CreateOneTimeCard command) {
        visitCreateCard(command);
    }

    @Override
    public void visitAddFunds(AddFunds command) {

    }

    @Override
    public ObjectNode visitDeleteAccount(DeleteAccount command) {
        return null;
    }

    @Override
    public void visitDeleteCard(DeleteCard command) {
        Account acc = command.findByIban(command.getAccount());
        if (acc == null) {
            return;
        }
        DeleteCardTransaction transaction = new DeleteCardTransaction("The card has been destroyed",
                command.getCardNumber(), command.getEmail(), command.getAccount(), command.getTimestamp());
        acc.getTransactions().add(transaction);
    }

    @Override
    public void visitSetMinBalance(SetMinBalance command) {

    }

    @Override
    public ObjectNode visitCheckCardStatus(CheckCardStatus command) {
        Account acc = command.findByCardNumber(command.getCardNumber());
        Card card = command.findByCardNumber(command.getCardNumber(), acc);

        if (card == null || acc == null) {
            return null;
        }
        if (acc.getBalance() <= acc.getMinBalance()) {
            Transaction transaction = new Transaction(command.getTimestamp(),
                    "You have reached the minimum amount of funds, the card will be frozen");
            acc.getTransactions().add(transaction);
            card.setStatus("frozen");
            return null;
        }
        if (acc.getBalance() <= acc.getMinBalance() + 30) {
            Transaction transaction = new Transaction(command.getTimestamp(), "Warning");
        }
        return null;
    }

    @Override
    public ObjectNode visitPayOnline(PayOnline command) {
        Account acc = command.findByCardNumber(command.getCardNumber(),
                command.findByEmail(command.getEmail()));
        if (acc == null) {
            return null;
        }
        Card card = command.findByCardNumber(command.getCardNumber(), acc);
        if (card == null) {
            return null;
        }
        if (card.getStatus().equals("frozen")) {
            Transaction transaction = new Transaction(command.getTimestamp(), "The card is frozen");
            acc.getTransactions().add(transaction);
            return null;
        }
        double amount = command.getAmount();

        if (!acc.getCurrency().equals(command.getCurrency())) {
            amount = command.exchangeMoney(command.getCurrency(), acc.getCurrency(), amount);
        }
        if (command.getPreviousBalance() < amount) {
            Transaction transaction = new Transaction(command.getTimestamp(), "Insufficient funds");
            acc.getTransactions().add(transaction);
            return null;
        }
//        if (acc.getMinBalance() >= command.getPreviousBalance() - amount) {
//            Transaction transaction = new Transaction(command.getTimestamp(),
//                    "You have reached the minimum amount of funds, the card will be frozen");
//            card.setStatus("frozen");
//            acc.getTransactions().add(transaction);
//            return null;
//        }
        CardPaymentTransaction transaction = new CardPaymentTransaction(amount,
                command.getCommerciant(), "Card payment", command.getTimestamp());

        acc.getTransactions().add(transaction);
        return null;
    }

    @Override
    public ObjectNode visitSendMoney(SendMoney command) {
        Account senderAcc = command.findByIban(command.getSenderAcc());
        Account receiverAcc = command.findByIban(command.getReceiverAcc());

        if (senderAcc == null || receiverAcc == null) {
            return null;
        }
        double amount = command.getAmount();
        if (!senderAcc.getCurrency().equals(receiverAcc.getCurrency())) {
            amount = command.exchangeMoney(senderAcc.getCurrency(), receiverAcc.getCurrency(), amount);
        }
        if (command.isInsufficientFunds()) {
            Transaction transaction = new Transaction(command.getTimestamp(), "Insufficient funds");
            senderAcc.getTransactions().add(transaction);
            return null;
        }
        SendMoneyTransaction sentTransaction = new SendMoneyTransaction(command.getTimestamp(),
                command.getDescription(), command.getSenderAcc(), command.getReceiverAcc(),
                command.getAmount(), "sent", senderAcc.getCurrency());
        SendMoneyTransaction receivedTransaction = new SendMoneyTransaction(command.getTimestamp(),
                command.getDescription(), command.getSenderAcc(), command.getReceiverAcc(),
                amount, "received", receiverAcc.getCurrency());


        senderAcc.getTransactions().add(sentTransaction);
        receiverAcc.getTransactions().add(receivedTransaction);
        return null;
    }

    @Override
    public void visitSetAlias(SetAlias command) {

    }

    @Override
    public ObjectNode visitPrintTransactions(PrintTransactions command) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode out = mapper.createObjectNode();
        Client client = command.findByEmail(command.getEmail());

        out.put("command", "printTransactions");
        out.put("output", client.transactionsToJson());
        out.put("timestamp", command.getTimestamp());
        return out;
    }

    @Override
    public void visitChangeInterest(ChangeInterestRate command) {
    }

    @Override
    public void visitSplitPayment(SplitPayment command) {
//        double individualAmount = command.getAmount() / command.getAccounts().size();
//        if (command.isSuccess()) {
//            for
//            SplitPaymentTransaction transaction = new SplitPaymentTransaction("Split payment of "
//                    + individualAmount + " " + command.getCurrency(), command.getTimestamp(),
//                    command.getCurrency(), command.getAmount(), command.getAccounts());
//
//        }
    }

    @Override
    public ObjectNode visitReport(Report command) {
        return null;
    }

    @Override
    public ObjectNode visitSpendingsReport(SpendingsReport command) {
        return null;
    }
}
