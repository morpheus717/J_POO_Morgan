package org.poo.main.patterns;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.command.AddAccount;
import org.poo.main.command.AddFunds;
import org.poo.main.command.CheckCardStatus;
import org.poo.main.command.CreateCard;
import org.poo.main.command.CreateOneTimeCard;
import org.poo.main.command.DeleteAccount;
import org.poo.main.command.DeleteCard;
import org.poo.main.command.PayOnline;
import org.poo.main.command.PrintTransactions;
import org.poo.main.command.PrintUsers;
import org.poo.main.command.Report;
import org.poo.main.command.SendMoney;
import org.poo.main.command.SetAlias;
import org.poo.main.command.SetMinBalance;
import org.poo.main.command.SplitPayment;
import org.poo.main.command.ChangeInterestRate;
import org.poo.main.command.SpendingsReport;
import org.poo.main.command.AddInterest;


/**
 * The visitor interface. Its purpose is to be implemented by the two visitors
 * that do the execution of each command, respectively the generation and saving of
 * transaction history for the commands that require them.
 */
public interface Visitor {
    /**
     * The purpose of this function is to be implemented by one of the two visitors
     */
    ObjectNode visitPrintUsers(PrintUsers command);

    /**
     * The purpose of this function is to be implemented by one of the two visitors
     */
    void visitAddAccount(AddAccount command);

    /**
     * The purpose of this function is to be implemented by one of the two visitors
     */
    void visitCreateCard(CreateCard command);

    /**
     * The purpose of this function is to be implemented by one of the two visitors
     */
    void visitCreateOneTimeCard(CreateOneTimeCard command);

    /**
     * The purpose of this function is to be implemented by one of the two visitors
     */
    void visitAddFunds(AddFunds command);

    /**
     * The purpose of this function is to be implemented by one of the two visitors
     */
    ObjectNode visitDeleteAccount(DeleteAccount command);

    /**
     * The purpose of this function is to be implemented by one of the two visitors
     */
    void visitDeleteCard(DeleteCard command);

    /**
     * The purpose of this function is to be implemented by one of the two visitors
     */
    void visitSetMinBalance(SetMinBalance command);

    /**
     * The purpose of this function is to be implemented by one of the two visitors
     */
    ObjectNode visitCheckCardStatus(CheckCardStatus command);

    /**
     * The purpose of this function is to be implemented by one of the two visitors
     */
    ObjectNode visitPayOnline(PayOnline command);

    /**
     * The purpose of this function is to be implemented by one of the two visitors
     */
    ObjectNode visitSendMoney(SendMoney command);

    /**
     * The purpose of this function is to be implemented by one of the two visitors
     */
    void visitSetAlias(SetAlias command);

    /**
     * The purpose of this function is to be implemented by one of the two visitors
     */
    ObjectNode visitPrintTransactions(PrintTransactions command);

    /**
     * The purpose of this function is to be implemented by one of the two visitors
     */
    ObjectNode visitChangeInterest(ChangeInterestRate command);

    /**
     * The purpose of this function is to be implemented by one of the two visitors
     */
    void visitSplitPayment(SplitPayment command);

    /**
     * The purpose of this function is to be implemented by one of the two visitors
     */
    ObjectNode visitReport(Report command);

    /**
     * The purpose of this function is to be implemented by one of the two visitors
     */
    ObjectNode visitSpendingsReport(SpendingsReport command);

    /**
     * The purpose of this function is to be implemented by one of the two visitors
     */
    ObjectNode visitAddInterest(AddInterest command);

}
