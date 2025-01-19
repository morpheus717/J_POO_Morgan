package org.poo.main;

import lombok.Getter;
import org.poo.main.user.Account;
import org.poo.main.user.Card;
import org.poo.main.user.Client;
import org.poo.utils.Pair;
import org.poo.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Bank {
    private static Bank instance;
    private static ArrayList<Client> clients;
    private static HashMap<Pair<String, String>, Double> exchanges;

    public ArrayList<Client> getClients() {
        return clients;
    }

    public HashMap<Pair<String, String>, Double> getExchanges() {
        return exchanges;
    }

    private Bank() {
        clients = new ArrayList<>();
        exchanges = new HashMap<>();
    }

    public static double getExchangeRate(
            final String currency1,
            final String currency2) {
        if (currency1.equals(currency2)) {
            return 1;
        }
        Double rate = exchanges.get(new Pair<>(currency1, currency2));
        if (rate != null) {
            return rate;
        }

        List<String> froms = new ArrayList<>();
        froms.add(currency1);

        for (int i = 0; i < froms.size(); i++) {
            String from = froms.get(i);
            for (String to : exchanges.keySet().stream()
                    .filter(pair -> pair.getValue0().equals(from))
                    .map(Pair::getValue1).toList()) {
                exchanges.put(new Pair<>(currency1, to),
                        getExchangeRate(currency1, from)
                                * getExchangeRate(from, to));
                if (to.equals(currency2)) {
                    return exchanges.get(new Pair<>(currency1, to));
                }
                froms.add(to);
            }
        }
        return exchanges.get(new Pair<>(currency1, currency2));
    }

    /**
     * Finds a client by their email address.
     *
     * @param email the email address to search for
     * @return the client matching the email, or null if not found
     */
    public static Client findByEmail(final String email) {
        return clients.stream().filter(c -> c.getEmail().equals(email)).findFirst().orElse(null);
    }

    /**
     * Finds an account by its IBAN for a given client.
     *
     * @param iban the IBAN of the account
     * @param client the client whose accounts should be searched
     * @return the account matching the IBAN, or null if not found
     */
    public static Account findByIban(final String iban, final Client client) {
        if (client == null) {
            return null;
        }
        return client.getAccounts().stream().filter(a -> a.getIban().equals(iban))
                .findFirst().orElse(null);
    }

    /**
     * Finds an account by its IBAN globally.
     *
     * @param iban the IBAN of the account
     * @return the account matching the IBAN, or null if not found
     */
    public static Account findByIban(final String iban) {
        for (final Client client : clients) {
            Account found = findByIban(iban, client);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    /**
     * Finds a card by card number for a given account.
     *
     * @param number the card number
     * @param account the account to search for the card
     * @return the account containing the card, or null if not found
     */
    public static Card findByNumber(final String number, final Account account) {
        if (account == null) {
            return null;
        }
        for (final Card card : account.getCards()) {
            if (card.getCardNumber().equals(number)) {
                return card;
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
    public static Card findByNumber(final String number, final Client client) {
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
     * Finds a card by its number globally.
     *
     * @param number the card number
     * @return the card matching the number, or null if not found
     */
    public static Card findByNumber(final String number) {
        for (final Client client : clients) {
            Card found = findByNumber(number, client);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    public void init() {
        Utils.resetRandom();
        clients.clear();
        exchanges.clear();
    }

    public static Bank getInstance() {
        if (instance == null) {
            instance = new Bank();
        }
        return instance;
    }
}
