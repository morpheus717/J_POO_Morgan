package org.poo.main.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.Bank;
import org.poo.main.command.management.AddInterest;
import org.poo.main.command.management.ChangeInterestRate;
import org.poo.main.command.Command;
import org.poo.main.command.payment.SendMoney;
import org.poo.main.patterns.AccountVisitor;
import org.poo.main.user.transactions.CardPaymentTransaction;
import org.poo.main.user.transactions.SendMoneyTransaction;
import org.poo.main.user.transactions.Transaction;

import java.util.ArrayList;

@Getter @Setter
public class Account implements AccountInterface {
    @JsonIgnore
    public static final int WARNING_THRESHOLD = 30;

    @JsonProperty("IBAN")
    private String iban;
    private double balance;
    private String currency;
    private String type;
    private ArrayList<Card> cards;
    @JsonIgnore
    private double minBalance;
    @JsonIgnore
    private ArrayList<Transaction> transactions;
    @JsonIgnore
    private double interestRate;
    @JsonIgnore
    private Client owner;

    public Account(final String iban, final double balance, final String currency,
                   final String type, Client owner) {
        this.iban = iban;
        this.balance = balance;
        this.currency = currency;
        this.type = type;
        this.cards = new ArrayList<>();
        this.minBalance = 0;
        this.transactions = new ArrayList<>();
        this.interestRate = 0;
        this.owner = owner;
    }

    public void withdraw(final double amount) {
        this.balance -= amount;
    }

    public void deposit(final double amount) {
        this.balance += amount;
    }

    public void sendMoney(SendMoney command, Account receiver) {
        if (command.getAmount() > this.balance) {
            this.transactions.add(new Transaction(command.getTimestamp(),
                    "Insufficient funds"));
            return;
        }
        double amountReceived = command.getAmount() *
                Bank.getExchangeRate(this.currency, receiver.currency);

        receiver.transactions.add(new SendMoneyTransaction(
                command.getTimestamp(),
                command.getDescription(),
                this.iban,
                receiver.iban,
                amountReceived,
                "received",
                receiver.currency));

        this.transactions.add(new SendMoneyTransaction(
                command.getTimestamp(),
                command.getDescription(),
                this.iban,
                receiver.iban,
                command.getAmount(),
                "sent",
                this.currency));
        this.withdraw(command.getAmount());
        receiver.deposit(amountReceived);
    }


    public ObjectNode destroyAccount(Command command) {
        if (this.balance != 0) {
            this.transactions.add(new Transaction(command.getTimestamp(),
                    "Account couldn't be deleted - there are funds remaining"));
            return new ObjectMapper().createObjectNode()
                    .put(
                            "error",
                            "Account couldn't be deleted - see org.poo.transactions for details"
                    )
                    .put("timestamp", command.getTimestamp());
        }

        for (Card card : this.cards) {
            card.destroy(command);
        }

        this.owner.getAccounts().remove(this);

        return new ObjectMapper().createObjectNode()
                .put("success", "Account deleted")
                .put("timestamp", command.getTimestamp());
    }

    /**
     * @return all transactions of this account
     */
    public ArrayNode transactionsToJson() {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode node = mapper.createArrayNode();
        for (Transaction transaction : transactions) {
            node.add(mapper.valueToTree(transaction));
        }
        return node;
    }

    /**
     * @return all transactions from startTimestamp to endTimestamp of this account in an ArrayNode
     */
    public ArrayNode transactionsToJson(final int startTimestamp, final int endTimestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode node = mapper.createArrayNode();
        for (Transaction transaction : transactions) {
            if (transaction.getTimestamp() >= startTimestamp
                    && transaction.getTimestamp() <= endTimestamp) {
                node.add(mapper.valueToTree(transaction));
            }
        }
        return node;
    }

    /**
     *
     * @param tranzactions Vector of transactions to extract commerciants from
     * @return Arraynode with desired output containing commerciants and the spent sums
     */
    public ArrayNode commerciantsToJson(final ArrayList<CardPaymentTransaction> tranzactions) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode node = mapper.createArrayNode();
        if (tranzactions.isEmpty()) {
            return node;
        }
        ArrayList<Commerciant> commerciants = new ArrayList<>();

        commerciants.add(new Commerciant(tranzactions.getFirst().getCommerciant(), 0.0));
        for (CardPaymentTransaction transaction : tranzactions) {
            Commerciant curr = commerciants.getLast();
            if (curr.getCommerciant().equals(transaction.getCommerciant())) {
                curr.setTotal(curr.getTotal() + transaction.getAmount());
            } else {
                commerciants.add(new Commerciant(transaction.getCommerciant(),
                        transaction.getAmount()));
            }
        }
        commerciants.sort((c1, c2) -> c1.getCommerciant().compareTo(c2.getCommerciant()));
        return mapper.valueToTree(commerciants);
    }

    /**
     * @return an ObjectNode containing an error because this Object represents a classic account
     */
    public ObjectNode modifyInterestRate(final ChangeInterestRate command) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode out = mapper.createObjectNode();
        ObjectNode out2 = mapper.createObjectNode();

        out2.put("description", "This is not a savings account");
        out2.put("timestamp", command.getTimestamp());
        out.put("command", "changeInterestRate");
        out.put("output", out2);
        out.put("timestamp", command.getTimestamp());
        return out;
    }

    /**
     * Function returns an error because this class represents the classic account
     */
    public ObjectNode addInterest(final AddInterest command) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        ObjectNode node2 = mapper.createObjectNode();

        node2.put("description", "This is not a savings account");
        node2.put("timestamp", command.getTimestamp());
        node.put("command", "addInterest");
        node.put("output", node2);
        node.put("timestamp", command.getTimestamp());
        return node;
    }

    public ObjectNode toJSON() {
        ObjectNode accountNode = new ObjectMapper().createObjectNode();
        accountNode.put("IBAN", iban);
        accountNode.put("balance", balance);
        accountNode.put("currency", currency);
        accountNode.put("type", type);

        ArrayNode cardsNode = new ObjectMapper().createArrayNode();
        for (Card card : cards) {
            cardsNode.add(card.toJSON());
        }
        accountNode.set("cards", cardsNode);
        return accountNode;
    }

    public ObjectNode accept(AccountVisitor visitor, int timestamp,
                       int startTimestamp, int endTimestamp) {
        return visitor.visit(this, timestamp, startTimestamp, endTimestamp);
    }

}
