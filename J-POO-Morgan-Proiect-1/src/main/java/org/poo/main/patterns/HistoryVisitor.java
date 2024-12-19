package org.poo.main.patterns;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.command.AddAccount;
import org.poo.main.command.AddFunds;
import org.poo.main.command.CheckCardStatus;
import org.poo.main.command.CreateCard;
import org.poo.main.command.CreateOneTimeCard;
import org.poo.main.command.DeleteAccount;
import org.poo.main.command.DeleteCard;
import org.poo.main.command.PayOnline;
import org.poo.main.command.PrintTransactions;
import org.poo.main.command.PrintUsers;
import org.poo.main.command.Report;
import org.poo.main.command.SendMoney;
import org.poo.main.command.SetAlias;
import org.poo.main.command.SetMinBalance;
import org.poo.main.command.SpendingsReport;
import org.poo.main.command.SplitPayment;
import org.poo.main.command.ChangeInterestRate;
import org.poo.main.command.AddInterest;
import org.poo.main.user.Account;
import org.poo.main.user.Card;
import org.poo.main.user.Client;
import org.poo.main.user.transactions.*;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * The purpose of this class is to handle the storage and printing of the history of transactions
 */
public class HistoryVisitor implements Visitor {

    private static final int WARNING_BALANCE_THRESHOLD = 30;

    /**
     * No history of transactions needs to be recorded, the purpose of the method is to
     * avoid errors
     */
    @Override
    public ObjectNode visitPrintUsers(final PrintUsers command) {
        return null;
    }

    /**
     * This method generates the transaction that represents successful creation of an account
     */
    @Override
    public void visitAddAccount(final AddAccount command) {
        AddAccountTransaction transaction = new AddAccountTransaction(command.getTimestamp());
        Account acc = command.findByIban(command.getGeneratedIban());
        acc.getTransactions().add(transaction);
    }

    /**
     * This method can not fail, it generates the transaction that represents
     * the successful creation of a card
     */
    @Override
    public void visitCreateCard(final CreateCard command) {
        CreateCardTransaction transaction = new CreateCardTransaction(command.getAccount(),
                command.getGeneratedNumber(), command.getEmail(), "New card created",
                command.getTimestamp());
        Account acc = command.findByIban(command.getAccount());
        acc.getTransactions().add(transaction);
    }

    /**
     * No history of transactions needs to be recorded, the purpose of the method is to
     * avoid errors
     */
    @Override
    public void visitCreateOneTimeCard(final CreateOneTimeCard command) {
        visitCreateCard(command);
    }

    /**
     * No history of transactions needs to be recorded, the purpose of the method is to
     * avoid errors
     */
    @Override
    public void visitAddFunds(final AddFunds command) {
        // No transaction history to record
    }

    /**
     * No history of transactions needs to be recorded, the purpose of the method is to
     * avoid errors
     */
    @Override
    public ObjectNode visitDeleteAccount(final DeleteAccount command) {
        return null;
    }

    /**
     * Method generates the transaction that represents the successful deletion of a card
     */
    @Override
    public void visitDeleteCard(final DeleteCard command) {
        Account acc = command.findByIban(command.getAccount());
        if (acc == null) {
            return;
        }
        DeleteCardTransaction transaction = new DeleteCardTransaction("The card has been destroyed",
                command.getCardNumber(), command.getEmail(), command.getAccount(),
                command.getTimestamp());
        acc.getTransactions().add(transaction);
    }

    /**
     * No history of transactions needs to be recorded, the purpose of the method is to
     * avoid errors
     */
    @Override
    public void visitSetMinBalance(final SetMinBalance command) {
        // No transaction history to record
    }

    /**
     * This method generates basic transactions in the case of an error while checking the card
     */
    @Override
    public ObjectNode visitCheckCardStatus(final CheckCardStatus command) {
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
        if (acc.getBalance() <= acc.getMinBalance() + WARNING_BALANCE_THRESHOLD) {
            Transaction transaction = new Transaction(command.getTimestamp(), "Warning");
            acc.getTransactions().add(transaction);
        }
        return null;
    }

    /**
     * This method generates and stores corresponding transactions for the failure of a payment.
     * It proved more efficient to handle the success transaction in the same function that
     * does the payment
     */
    @Override
    public ObjectNode visitPayOnline(final PayOnline command) {
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
            Transaction transaction = new Transaction(command.getTimestamp(),
                    "The card is frozen");
            acc.getTransactions().add(transaction);
            return null;
        }
        double amount = command.getAmount();

        if (!acc.getCurrency().equals(command.getCurrency())) {
            amount = command.exchangeMoney(command.getCurrency(), acc.getCurrency(), amount);
        }
        if (command.getPreviousBalance() < amount) {
            Transaction transaction = new Transaction(command.getTimestamp(),
                    "Insufficient funds");
            acc.getTransactions().add(transaction);
            return null;
        }
        return null;
    }

    /**
     * Method generates and stores transactions for both the sender and
     * receiver of a sendMoney command, or and Insufficient funds error transaction otherwise
     */
    @Override
    public ObjectNode visitSendMoney(final SendMoney command) {
        Account senderAcc = command.findByIban(command.getSenderAcc());
        Account receiverAcc = command.findByIban(command.getReceiverAcc());

        if (senderAcc == null || receiverAcc == null) {
            return null;
        }
        double amount = command.getAmount();
        if (!senderAcc.getCurrency().equals(receiverAcc.getCurrency())) {
            amount = command.exchangeMoney(senderAcc.getCurrency(), receiverAcc.getCurrency(),
                    amount);
        }
        if (command.isInsufficientFunds()) {
            Transaction transaction = new Transaction(command.getTimestamp(),
                    "Insufficient funds");
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

    /**
     * No history of transactions needs to be recorded, the purpose of the method is to
     * avoid errors
     */
    @Override
    public void visitSetAlias(final SetAlias command) {
        // No transaction history to record
    }

    /**
     *
     * @return An Object Node that contains all transactions of a client
     */
    @Override
    public ObjectNode visitPrintTransactions(final PrintTransactions command) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode out = mapper.createObjectNode();
        Client client = command.findByEmail(command.getEmail());

        out.put("command", "printTransactions");
        out.put("output", client.transactionsToJson());
        out.put("timestamp", command.getTimestamp());
        return out;
    }

    /**
     * Method creates and stores transaction that shows interest has been changed
     */
    @Override
    public ObjectNode visitChangeInterest(final ChangeInterestRate command) {
        Account acc = command.findByIban(command.getAccount());
        Transaction transaction = new Transaction(command.getTimestamp(),
                String.format("Interest rate of the account changed to %.2f",
                        command.getInterestRate()));
        acc.getTransactions().add(transaction);
        return null;
    }

    /**
     * avoid errors
     */
    @Override
    public void visitSplitPayment(final SplitPayment command) {
    }

    /**
     * Displays the report of transactions from startTimestamp to endTimestamp
     */
    @Override
    public ObjectNode visitReport(final Report command) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode out = mapper.createObjectNode();
        ObjectNode out2 = mapper.createObjectNode();
        Account acc = command.findByIban(command.getAccount());

        if (acc == null) {
            return command.accountNotFound();
        }
        out2.put("IBAN", acc.getIban());
        out2.put("balance", acc.getBalance());
        out2.put("currency", acc.getCurrency());
        out2.put("transactions", acc.transactionsToJson(command.getStartTimestamp(),
                command.getEndTimestamp()));
        out.put("command", "report");
        out.put("output", out2);
        out.put("timestamp", command.getTimestamp());
        return out;
    }

    /**
     * Displays the history of transactions that are considered "spending money"
     */
    @Override
    public ObjectNode visitSpendingsReport(final SpendingsReport command) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode out = mapper.createObjectNode();
        ObjectNode out2 = mapper.createObjectNode();
        Account acc = command.findByIban(command.getAccount());
        ArrayList<CardPaymentTransaction> spendings = new ArrayList<>();

        if (acc == null) {
            return command.accountNotFound();
        }
        out.put("command", "spendingsReport");
        out2.put("IBAN", acc.getIban());
        out2.put("balance", acc.getBalance());
        out2.put("currency", acc.getCurrency());
        for (Transaction transaction : acc.getTransactions()) {
            if (transaction.filterSpendings() != null && transaction.getTimestamp()
                    >= command.getStartTimestamp() && transaction.getTimestamp()
                    <= command.getEndTimestamp()) {
                spendings.add(transaction.filterSpendings());
            }
        }
        out2.put("transactions", acc.transactionsToJson(command.getStartTimestamp(),
                command.getEndTimestamp(), spendings));
        spendings.sort(Comparator.comparing(CardPaymentTransaction::getCommerciant));
        out2.put("commerciants", acc.commerciantsToJson(spendings));
        out.put("output", out2);
        out.put("timestamp", command.getTimestamp());
        return out;
    }

    /**
     * avoid errors
     */
    @Override
    public ObjectNode visitAddInterest(final AddInterest command) {
        return null;
    }
}
