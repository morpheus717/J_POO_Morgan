package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Account;
import org.poo.main.user.Card;
import org.poo.main.user.Client;

import java.util.ArrayList;

@Getter @Setter
public abstract class Command {
    private int timestamp;
    private ArrayList<Client> clients;

    public Command(final ArrayList<Client> clients, final int timestamp) {
        this.timestamp = timestamp;
        this.clients = clients;
    }

    /**
     * Finds a client by their email address.
     *
     * @param email the email address to search for
     * @return the client matching the email, or null if not found
     */
    public Client findByEmail(final String email) {
        return clients.stream().filter(c -> c.getEmail().equals(email)).findFirst().orElse(null);
    }

    /**
     * Finds an account by its IBAN for a given client.
     *
     * @param iban the IBAN of the account
     * @param client the client whose accounts should be searched
     * @return the account matching the IBAN, or null if not found
     */
    public Account findByIban(final String iban, final Client client) {
        if (client == null) {
            return null;
        }
        return client.getAccounts().stream().filter(a -> a.getIban().equals(iban))
                .findFirst().orElse(null);
    }

    /**
     * Finds an account by its IBAN.
     *
     * @param iban the IBAN of the account
     * @return the account matching the IBAN, or null if not found
     */
    public Account findByIban(final String iban) {
        for (final Client client : clients) {
            Account found = findByIban(iban, client);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    /**
     * Finds a card by its number for a given client.
     *
     * @param number the card number
     * @param client the client whose accounts should be searched
     * @return the card matching the number, or null if not found
     */
    public Card findByNumber(final String number, final Client client) {
        if (client == null) {
            return null;
        }
        for (final Account account : client.getAccounts()) {
            for (final Card card : account.getCards()) {
                if (card.getCardNumber().equals(number)) {
                    return card;
                }
            }
        }
        return null;
    }

    /**
     * Finds a card by its number.
     *
     * @param number the card number
     * @return the card matching the number, or null if not found
     */
    public Card findByNumber(final String number) {
        for (final Client client : clients) {
            Card found = findByNumber(number, client);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    /**
     * Finds an account by card number for a given client.
     *
     * @param cardNumber the card number
     * @param client the client whose accounts should be searched
     * @return the account containing the card, or null if not found
     */
    public Account findByCardNumber(final String cardNumber, final Client client) {
        if (client == null) {
            return null;
        }
        for (final Account account : client.getAccounts()) {
            for (final Card card : account.getCards()) {
                if (card.getCardNumber().equals(cardNumber)) {
                    return account;
                }
            }
        }
        return null;
    }

    /**
     * Finds an account by card number for a given account.
     *
     * @param cardNumber the card number
     * @param account the account to search for the card
     * @return the account containing the card, or null if not found
     */
    public Card findByCardNumber(final String cardNumber, final Account account) {
        if (account == null) {
            return null;
        }
        for (final Card card : account.getCards()) {
            if (card.getCardNumber().equals(cardNumber)) {
                return card;
            }
        }
        return null;
    }

    /**
     * Finds an account by alias for a given client.
     *
     * @param alias the alias to search for
     * @param client the client whose accounts should be searched
     * @return the account matching the alias, or null if not found
     */
    public Account findByAlias(final String alias, final Client client) {
        final String acc = client.getAliases().get(alias);
        if (acc != null) {
            return findByIban(acc, client);
        }
        return null;
    }

    /**
     * Finds an account by alias.
     *
     * @param alias the alias to search for
     * @return the account matching the alias, or null if not found
     */
    public Account findByAlias(final String alias) {
        for (final Client client : clients) {
            final String acc = client.getAliases().get(alias);
            if (acc != null) {
                return findByIban(acc, client);
            }
        }
        return null;
    }

    /**
     * Finds an account by card number.
     *
     * @param cardNumber the card number
     * @return the account containing the card, or null if not found
     */
    public Account findByCardNumber(final String cardNumber) {
        for (final Client client : clients) {
            for (final Account account : client.getAccounts()) {
                for (final Card card : account.getCards()) {
                    if (card.getCardNumber().equals(cardNumber)) {
                        return account;
                    }
                }
            }
        }
        return null;
    }

    /**
     * This function will be implemented by all children to execute their corresponding commands
     */
    public abstract ObjectNode accept(Visitor visitor);
}
