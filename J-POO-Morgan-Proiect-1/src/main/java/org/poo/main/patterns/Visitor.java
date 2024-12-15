package org.poo.main.patterns;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.command.*;
import org.poo.main.user.Client;

import java.util.ArrayList;

public interface Visitor {
    public ObjectNode visitPrintUsers(PrintUsers command);

    public void visitAddAccount(AddAccount command);

    public void visitCreateCard(CreateCard command);

    public void visitCreateOneTimeCard(CreateOneTimeCard command);

    public void visitAddFunds(AddFunds command);

    public ObjectNode visitDeleteAccount(DeleteAccount command);

    public void visitDeleteCard(DeleteCard command);

    public void visitSetMinBalance(SetMinBalance command);

    public ObjectNode visitCheckCardStatus(CheckCardStatus command);
}
