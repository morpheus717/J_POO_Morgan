package org.poo.main.patterns;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.command.*;
import org.poo.main.user.Account;
import org.poo.main.user.Card;
import org.poo.main.user.Client;
import org.poo.main.user.OneTimeCard;
import org.poo.utils.Utils;

public class ExecuteVisitor implements Visitor {
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

        if (client != null) {
            client.getAccounts().add(new Account(Utils.generateIBAN(), 0.0, command.getCurrency(),
                    command.getAccountType()));
        }
    }

    public void visitCreateCard(CreateCard command) {
        Account account = command.findByIban(command.getAccount(),
                command.findByEmail(command.getEmail()));
        if (account == null)
            return;
        account.getCards().add(new Card(Utils.generateCardNumber(), "active"));
    }

    public void visitCreateOneTimeCard(CreateOneTimeCard command) {
        Account account = command.findByIban(command.getAccount(),
                command.findByEmail(command.getEmail()));
        account.getCards().add(new OneTimeCard(Utils.generateCardNumber(), "active"));
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
        Card card = command.findByNumber(command.getCardNumber(), client);
        if (card != null) {
            for (Account acc : client.getAccounts()) {
                acc.getCards().remove(card);
            }
        }
    }

    public void visitSetMinBalance(SetMinBalance command) {
        command.findByIban(command.getAccountName()).setMinBalance(command.getAmount());
    }

    public ObjectNode visitCheckCardStatus(CheckCardStatus command) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode out = mapper.createObjectNode();
        out.put("command", "checkCardStatus");

    }
}
