package org.poo.main.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.command.AddInterest;
import org.poo.main.command.ChangeInterestRate;
import org.poo.main.user.transactions.CardPaymentTransaction;
import org.poo.main.user.transactions.Transaction;

import java.util.ArrayList;

@Getter @Setter
public class Account {
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

    public Account(final String iban, final double balance, final String currency,
                   final String type) {
        this.iban = iban;
        this.balance = balance;
        this.currency = currency;
        this.type = type;
        this.cards = new ArrayList<>();
        this.minBalance = 0;
        this.transactions = new ArrayList<>();
        this.interestRate = 0;
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
     * @return Transactions of the provided ArrayList from startTimestamp and
     * endTimestamp in an ArrayNode
     */
    public ArrayNode transactionsToJson(final int startTimestamp, final int endTimestamp,
                                        final ArrayList<CardPaymentTransaction> tranzactions) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode node = mapper.createArrayNode();
        for (CardPaymentTransaction transaction : tranzactions) {
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

}
