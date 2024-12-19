package org.poo.main.patterns;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.command.AddAccount;
import org.poo.main.command.AddFunds;
import org.poo.main.user.Card;
import org.poo.main.user.OneTimeCard;
import org.poo.main.command.AddInterest;
import org.poo.main.command.SpendingsReport;
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
import org.poo.main.command.SplitPayment;
import org.poo.main.command.ChangeInterestRate;
import org.poo.main.user.Account;
import org.poo.main.user.Client;
import org.poo.main.user.SavingsAccount;
import org.poo.main.user.transactions.FailedSplitPayment;
import org.poo.main.user.transactions.SplitPaymentTransaction;
import org.poo.main.user.transactions.Transaction;
import org.poo.utils.Utils;

import java.util.ArrayList;

public class ExecuteVisitor implements Visitor {

    /**
     * The method returns an Object Node that contains all active users
     */
    @Override
    public ObjectNode visitPrintUsers(final PrintUsers command) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode out = mapper.createObjectNode();
        out.put("command", "printUsers");
        out.put("output", mapper.valueToTree(command.getClients()));
        out.put("timestamp", command.getTimestamp());
        return out;
    }

    /**
     * The method creates a new account and adds it to the given client
     */
    public void visitAddAccount(final AddAccount command) {
        Client client = command.findByEmail(command.getEmail());
        command.setGeneratedIban(Utils.generateIBAN());

        if (client != null) {
            if (command.getAccountType().equals("classic")) {
                client.getAccounts().add(new Account(command.getGeneratedIban(), 0.0,
                        command.getCurrency(), command.getAccountType()));
            } else if (command.getAccountType().equals("savings")) {
                client.getAccounts().add(new SavingsAccount(command.getGeneratedIban(), 0.0,
                        command.getCurrency(), command.getAccountType()));
            }
        }
    }

    /**
     * The method creates a card and attaches it to the given account
     */
    public void visitCreateCard(final CreateCard command) {
        Account account = command.findByIban(command.getAccount(),
                command.findByEmail(command.getEmail()));
        command.setGeneratedNumber(Utils.generateCardNumber());
        if (account == null) {
            return;
        }
        account.getCards().add(new Card(command.getGeneratedNumber(), "active"));
    }

    /**
     * The method creates a one time card and attaches it to the given account
     */
    public void visitCreateOneTimeCard(final CreateOneTimeCard command) {
        Account account = command.findByIban(command.getAccount(),
                command.findByEmail(command.getEmail()));
        command.setGeneratedNumber(Utils.generateCardNumber());
        account.getCards().add(new OneTimeCard(command.getGeneratedNumber(), "active"));
    }

    /**
     * This method modifies the balance of an account after finding it
     */
    public void visitAddFunds(final AddFunds command) {
        Account acc = command.findByIban(command.getAccount());
        acc.setBalance(acc.getBalance() + command.getAmount());
    }

    /**
     * This method finds the account and the client it is attached to and deletes it
     */
    public ObjectNode visitDeleteAccount(final DeleteAccount command) {
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
            Transaction transaction = new Transaction(command.getTimestamp(),
                    "Account couldn't be deleted - there are funds remaining");
            toBeDeleted.getTransactions().add(transaction);
            return out;
        }
        client.getAccounts().remove(toBeDeleted);
        out2.put("success", "Account deleted");
        out2.put("timestamp", command.getTimestamp());
        out.put("output", out2);
        out.put("timestamp", command.getTimestamp());
        return out;
    }

    /**
     * This method finds a card and the account it is attached to and deletes the card
     */
    public void visitDeleteCard(final DeleteCard command) {
        Client client = command.findByEmail(command.getEmail());
        Account acc = command.findByCardNumber(command.getCardNumber(), client);
        Card card = command.findByNumber(command.getCardNumber(), client);
        if (acc == null) {
            return;
        }
        command.setAccount(acc.getIban());
        acc.getCards().remove(card);
    }

    /**
     * This method sets a minimum balance that will block a card if it gets exceeded,
     * but only if we check the status of the card, as the task demands.
     * It's like a Schrodinger's card.
     */
    public void visitSetMinBalance(final SetMinBalance command) {
        Account acc = command.findByIban(command.getAccountName());
        acc.setMinBalance(command.getAmount());
    }

    /**
     * This method checks the status of a card after finding it
     */
    public ObjectNode visitCheckCardStatus(final CheckCardStatus command) {
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

    /**
     * This method executes an online payment based on the command while taking
     * into account all the corner cases
     */
    public ObjectNode visitPayOnline(final PayOnline command) {
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
        Card card = command.findByNumber(command.getCardNumber());
        if (card == null) {
            return out;
        }
        if (command.findByNumber(command.getCardNumber(), client) == null) {
            return null;
        }

        Account acc = command.findByCardNumber(command.getCardNumber(), client);
        command.setPreviousBalance(acc.getBalance());

        if (card.getStatus().equals("frozen")) {
            return null;
        }
        card.pay(acc, command);
        return null;
    }

    /**
     * This method handles the execution of the send money command and also
     * takes into account all the corner cases
     */
    public ObjectNode visitSendMoney(final SendMoney command) {
        Client senderClient = command.findByEmail(command.getSenderEmail());

        if (senderClient == null) {
            return null;
        }
        Account sender = command.findByIban(command.getSenderAcc(), senderClient);

        if (sender == null) {
            return null;
        }
        Account receiver = command.findByIban(command.getReceiverAcc());

        if (receiver == null) {
            receiver = command.findByAlias(command.getReceiverAcc());
        }
        if (receiver == null) {
            return null;
        }
        if (sender.getBalance() < command.getAmount()) {
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

    /**
     * The method finds the client that wants to set an alias and then sets it
     */
    public void visitSetAlias(final SetAlias command) {
        Client client = command.findByEmail(command.getEmail());
        client.getAliases().put(command.getAlias(), command.getAccIban());
    }

    /**
     * avoid errors
     */
    @Override
    public ObjectNode visitPrintTransactions(final PrintTransactions command) {
        return null;
    }

    /**
     * This function changes the interest rate after finding the corresponding account
     */
    public ObjectNode visitChangeInterest(final ChangeInterestRate command) {
        Account acc = command.findByIban(command.getAccount());
        return acc.modifyInterestRate(command);
    }

    /**
     * It has been proved significantly more efficient to execute the command
     * and generate and store the transaction at the same time to avoid excessive memory usage
     * so with the risk of the method being messy and too long, an exception was made
     * and this function does that
     */
    public void visitSplitPayment(final SplitPayment command) {
        double individualAmount = command.getAmount() / command.getAccounts().size();
        ArrayList<Account> accounts = new ArrayList<>();
        for (int i = command.getAccounts().size() - 1; i >= 0; i--) {
            Account acc = command.findByIban(command.getAccounts().get(i));
            if (acc == null) {
                return; // account not found
            }
            if (acc.getBalance() < individualAmount) {
                for (int j = 0; j < command.getAccounts().size(); j++) {
                    Account acc2 = command.findByIban(command.getAccounts().get(j));
                    FailedSplitPayment transaction = new FailedSplitPayment(String
                            .format("Account %s has insufficient funds for a split payment.",
                                    acc.getIban()), String.format("Split payment of %.2f %s",
                            command.getAmount(), command.getCurrency()), command.getTimestamp(),
                            command.getCurrency(), individualAmount, command.getAccounts());
                    acc2.getTransactions().add(transaction);
                }
                return; // not enough money
            }
            accounts.add(acc);
        }
        for (Account acc : accounts) {
            double convertedAmount = command.exchangeMoney(command.getCurrency(),
                    acc.getCurrency(), individualAmount);
            acc.setBalance(acc.getBalance() - convertedAmount);
            SplitPaymentTransaction transaction = new SplitPaymentTransaction(
                    String.format("Split payment of %.2f %s", command.getAmount(),
                            command.getCurrency()),
                    command.getTimestamp(), command.getCurrency(), individualAmount,
                    command.getAccounts());
            acc.getTransactions().add(transaction);
        }
        command.setSuccess(true);
    }

    /**
     * avoid errors
     */
    public ObjectNode visitReport(final Report command) {
        return null;
    }

    /**
     * avoid errors
     */
    public ObjectNode visitSpendingsReport(final SpendingsReport command) {
        return null;
    }

    /**
     * The method calls the necessary function from the account class to modify the balance
     */
    public ObjectNode visitAddInterest(final AddInterest command) {
        Account acc = command.findByIban(command.getAccount());
        return acc.addInterest(command);
    }
}
