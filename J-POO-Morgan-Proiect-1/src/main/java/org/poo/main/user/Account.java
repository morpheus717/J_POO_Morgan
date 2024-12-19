package org.poo.main.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.command.Report;
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

    public Account(String iban, double balance, String currency, String type) {
        this.iban = iban;
        this.balance = balance;
        this.currency = currency;
        this.type = type;
        this.cards = new ArrayList<>();
        this.minBalance = 0;
        this.transactions = new ArrayList<>();
        this.interestRate = 0;
    }

    public ArrayNode transactionsToJson() {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode node = mapper.createArrayNode();
        for (Transaction transaction : transactions) {
            node.add(mapper.valueToTree(transaction));
        }
        return node;
    }

    public ArrayNode transactionsToJson(int startTimestamp, int endTimestamp) {
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

    public ArrayNode transactionsToJson(int startTimestamp, int endTimestamp,
                                        ArrayList<CardPaymentTransaction> transactions) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode node = mapper.createArrayNode();
        for (CardPaymentTransaction transaction : transactions) {
            if (transaction.getTimestamp() >= startTimestamp
                    && transaction.getTimestamp() <= endTimestamp) {
                node.add(mapper.valueToTree(transaction));
            }
        }
        return node;
    }

    public ArrayNode commerciantsToJson(ArrayList<CardPaymentTransaction> transactions) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode node = mapper.createArrayNode();
        if (transactions.isEmpty()) {
            return node;
        }
        ArrayList<Commerciant> commerciants = new ArrayList<>();

        commerciants.add(new Commerciant(transactions.getFirst().getCommerciant(), 0.0));
        for (CardPaymentTransaction transaction : transactions) {
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
    public void modifyInterestRate(double interestRate) {

    }

}
