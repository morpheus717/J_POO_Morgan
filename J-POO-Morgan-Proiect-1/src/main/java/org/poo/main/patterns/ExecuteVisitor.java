package org.poo.main.patterns;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.command.*;
import org.poo.main.user.*;
import org.poo.main.user.transactions.CardPaymentTransaction;
import org.poo.main.user.transactions.SplitPaymentTransaction;
import org.poo.main.user.transactions.Transaction;
import org.poo.utils.Utils;

import java.util.ArrayList;
import java.util.Comparator;

public class ExecuteVisitor implements Visitor {

    public ObjectNode createErrorResponse(String messageName, String message, int timestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode errorResponse = mapper.createObjectNode();
        errorResponse.put("timestamp", timestamp);
        errorResponse.put(messageName, message);
        return errorResponse;
    }

    @Override
    public ObjectNode visitPrintUsers(PrintUsers command) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode out = mapper.createObjectNode();
        out.put("command", "printUsers");
        out.put("output", mapper.valueToTree(command.getClients()));
        out.put("timestamp", command.getTimestamp());
        return out;
    }

    public void visitAddAccount(AddAccount command) {
        Client client = command.findByEmail(command.getEmail());
        command.setGeneratedIban(Utils.generateIBAN());

        if (client != null) {
            if (command.getAccountType().equals("classic"))
                client.getAccounts().add(new Account(command.getGeneratedIban(), 0.0,
                        command.getCurrency(), command.getAccountType()));
            else if (command.getAccountType().equals("savings"))
                client.getAccounts().add(new SavingsAccount(command.getGeneratedIban(), 0.0,
                        command.getCurrency(), command.getAccountType()));
        }
    }

    public void visitCreateCard(CreateCard command) {
        Account account = command.findByIban(command.getAccount(),
                command.findByEmail(command.getEmail()));
        command.setGeneratedNumber(Utils.generateCardNumber());
        if (account == null)
            return;
        account.getCards().add(new Card(command.getGeneratedNumber(), "active"));
    }

    public void visitCreateOneTimeCard(CreateOneTimeCard command) {
        Account account = command.findByIban(command.getAccount(),
                command.findByEmail(command.getEmail()));
        command.setGeneratedNumber(Utils.generateCardNumber());
        account.getCards().add(new OneTimeCard(command.getGeneratedNumber(), "active"));
    }

    public void visitAddFunds(AddFunds command) {
        Account acc = command.findByIban(command.getAccount());
        acc.setBalance(acc.getBalance() + command.getAmount());
    }

    public ObjectNode visitDeleteAccount(DeleteAccount command) {
        Client client = command.findByEmail(command.getEmail());
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode out = mapper.createObjectNode();
        ObjectNode out2 = mapper.createObjectNode();

        out.put("command", "deleteAccount");
        Account toBeDeleted = command.findByIban(command.getAccountName());
        if (toBeDeleted == null) {
            return null;
        }
        if (toBeDeleted.getBalance() != 0) {
            out2.put("error", "Account couldn't be deleted - see org.poo.transactions for details");
            out2.put("timestamp", command.getTimestamp());
            out.put("output", out2);
            out.put("timestamp", command.getTimestamp());
            return out;
        }
        client.getAccounts().remove(toBeDeleted);
        out2.put("success", "Account deleted");
        out2.put("timestamp", command.getTimestamp());
        out.put("output", out2);
        out.put("timestamp", command.getTimestamp());
        return out;
    }

    public void visitDeleteCard(DeleteCard command) {
        Client client = command.findByEmail(command.getEmail());
        Account acc = command.findByCardNumber(command.getCardNumber(), client);
        Card card = command.findByNumber(command.getCardNumber(), client);
        if (acc == null) {
            return;
        }
        command.setAccount(acc.getIban());
        acc.getCards().remove(card);
    }

    public void visitSetMinBalance(SetMinBalance command) {
        Account acc = command.findByIban(command.getAccountName());
        acc.setMinBalance(command.getAmount());
    }

    public ObjectNode visitCheckCardStatus(CheckCardStatus command) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode out = mapper.createObjectNode();
        Account acc = command.findByCardNumber(command.getCardNumber());
        Card card = command.findByCardNumber(command.getCardNumber(), acc);

        if (card == null) {
            out.put("command", "checkCardStatus");
            ObjectNode out2 = mapper.createObjectNode();
            out2.put("timestamp", command.getTimestamp());
            out2.put("description", "Card not found");
            out.put("output", out2);
            out.put("timestamp", command.getTimestamp());
            return out;
        }

        return null;
    }

    public ObjectNode visitPayOnline(PayOnline command) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode out = mapper.createObjectNode();
        ObjectNode out2 = mapper.createObjectNode();

        out2.put("timestamp", command.getTimestamp());
        out2.put("description", "Card not found");
        out.put("command", "payOnline");
        out.put("output", out2);
        out.put("timestamp", command.getTimestamp());
        Client client = command.findByEmail(command.getEmail());

        if (client == null) {
            return null;
        }
        Account acc = command.findByCardNumber(command.getCardNumber(), client);

        if (acc == null) {
            return out;
        }
        command.setPreviousBalance(acc.getBalance());
        Card card = command.findByCardNumber(command.getCardNumber(), acc);

        if (card == null) {
            return out;
        }
        if (card.getStatus().equals("frozen")) {
            return null;
        }
        card.pay(acc, command);
        return null;
    }

    public ObjectNode visitSendMoney(SendMoney command) {
        Client senderClient = command.findByEmail(command.getSenderEmail());

        if (senderClient == null) {
            // return createErrorResponse("Invalid sender email");
            return null;
        }
        Account sender = command.findByIban(command.getSenderAcc(), senderClient);

        if (sender == null) {
            return null;
        }
        Account receiver = command.findByIban(command.getReceiverAcc());

        if (receiver == null) {
            receiver = command.findByAlias(command.getReceiverAcc());
            // aparent aliasurile sunt globale
        }
        if (receiver == null) {
            return null;
        }
        if (sender.getBalance() < command.getAmount()) {
            // return createErrorResponse("Invalid sender or receiver");
            command.setInsufficientFunds(true);
            return null;
        }
        sender.setBalance(sender.getBalance() - command.getAmount());
        if (sender.getCurrency().equals(receiver.getCurrency())) {
            receiver.setBalance(receiver.getBalance() + command.getAmount());
            return null;
        }
        double receivedMoney = command.exchangeMoney(sender.getCurrency(), receiver.getCurrency(),
                                                     command.getAmount());
        receiver.setBalance(receiver.getBalance() + receivedMoney);
        return null;
    }

    public void visitSetAlias(SetAlias command) {
        Client client = command.findByEmail(command.getEmail());
        client.getAliases().put(command.getAlias(), command.getAccIban());
    }

    @Override
    public ObjectNode visitPrintTransactions(PrintTransactions command) {
        return null;
    }

    public void visitChangeInterest(ChangeInterestRate command) {
        Account acc = command.findByIban(command.getAccount());
        acc.modifyInterestRate(command.getInterestRate());
    }

    public void visitSplitPayment(SplitPayment command) {
        double individualAmount = command.getAmount() / command.getAccounts().size();
        ArrayList<Account> accounts = new ArrayList<>();
        for (int i = 0; i < command.getAccounts().size(); i++) {
            Account acc = command.findByIban(command.getAccounts().get(i));
            if (acc == null) {
                return; // account not found
            }
            if (acc.getBalance() < individualAmount) {
                return; // not enough money
            }
            accounts.add(acc);
        }
        for (Account acc : accounts) {
            double convertedAmount = command.exchangeMoney(command.getCurrency(),
                    acc.getCurrency(), individualAmount);
            acc.setBalance(acc.getBalance() - convertedAmount);
            SplitPaymentTransaction transaction = new SplitPaymentTransaction(
                    String.format("Split payment of %.2f %s", command.getAmount(), command.getCurrency()),
                    command.getTimestamp(), command.getCurrency(), individualAmount, command.getAccounts());
            acc.getTransactions().add(transaction);
        }
        command.setSuccess(true);
    }

    public ObjectNode visitReport(Report command) {
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

    public ObjectNode visitSpendingsReport(SpendingsReport command) {
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
                    >= command.getStartTimestamp() && transaction.getTimestamp() <= command.getEndTimestamp()) {
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
}
