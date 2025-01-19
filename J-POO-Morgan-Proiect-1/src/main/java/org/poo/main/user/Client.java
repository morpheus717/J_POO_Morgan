package org.poo.main.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.user.transactions.Transaction;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a client with accounts and aliases, capable of converting transactions to JSON.
 */
@Getter
@Setter
public class Client {
    private String firstName;
    private String lastName;
    private String email;
    private ArrayList<Account> accounts;

    @JsonIgnore
    private HashMap<String, String> aliases;

    public Client(final String firstName, final String lastName, final String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.accounts = new ArrayList<>();
        this.aliases = new HashMap<>();
    }

    /**
     * Converts all transactions from all accounts to a JSON array.
     * <p>
     * This method collects transactions from all associated accounts,
     * sorts them by timestamp, and serializes them into a JSON array.
     *
     * @return a JSON array representing the sorted transactions
     */
    public ArrayNode transactionsToJson() {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Transaction> transactions = new ArrayList<>();
        for (Account account : accounts) {
            transactions.addAll(account.getTransactions());
        }
        transactions.sort((t1, t2) -> t1.getTimestamp() - t2.getTimestamp());
        return mapper.valueToTree(transactions);
    }

    public ObjectNode toJSON() {
        ObjectNode userNode = new ObjectMapper().createObjectNode();
        userNode.put("firstName", firstName);
        userNode.put("lastName", lastName);
        userNode.put("email", email);

        ArrayNode accountsNode = new ObjectMapper().createArrayNode();
        for (Account account : accounts) {
            accountsNode.add(account.toJSON());
        }
        userNode.set("accounts", accountsNode);
        return userNode;
    }
}
