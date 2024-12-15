package org.poo.main.user;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
public class Client {
    private String firstName;
    private String lastName;
    private String email;
    private ArrayList<Account> accounts;

    public Client(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.accounts = new ArrayList<>();
    }
}
