package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import org.poo.fileio.*;
import org.poo.main.command.*;
import org.poo.main.patterns.ExecuteVisitor;
import org.poo.main.patterns.HistoryVisitor;
import org.poo.main.user.Client;
import org.poo.main.user.Exchange;
import org.poo.utils.Pair;
import org.poo.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BankManager {
    private static BankManager instance;
    private static ArrayList<Client> clients;
    private static HashMap<Pair<String, String>, Double> exchanges;

    public ArrayList<Client> getClients() {
        return clients;
    }

    public HashMap<Pair<String, String>, Double> getExchanges() {
        return exchanges;
    }

    private BankManager() {
        clients = new ArrayList<>();
        exchanges = new HashMap<>();
    }

    public double getExchangeRate(
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


    public static BankManager getInstance() {
        if (instance == null) {
            instance = new BankManager();
        }
        Utils.resetRandom();
        clients.clear();
        exchanges.clear();
        return instance;
    }
}
