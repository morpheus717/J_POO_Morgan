package org.poo.main.patterns;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.user.Account;

public interface AccountVisitor {

    public ObjectNode visit(Account account, int timestamp,
                            int startTimestamp, int endTimestamp);

}
