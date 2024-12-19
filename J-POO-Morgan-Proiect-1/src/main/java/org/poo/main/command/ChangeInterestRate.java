package org.poo.main.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.main.patterns.Visitor;
import org.poo.main.user.Client;

import java.util.ArrayList;

@Getter @Setter
public class ChangeInterestRate extends Command {
    private String account;
    private double interestRate;
    private ArrayList<Client> clients;

    public ChangeInterestRate(ArrayList<Client> clients, String account,
                              double interestRate, int timestamp) {
        super(clients, timestamp);
        this.account = account;
        this.interestRate = interestRate;
    }

    public ObjectNode accept(Visitor visitor) {
        visitor.visitChangeInterest(this);
        return null;
    }
}
