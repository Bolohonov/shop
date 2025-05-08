package org.example.payment;

import org.example.payment.model.Account;

public class CreateTestAccountService {
    public static Account createDefaultAccount() {
        Account account = new Account();
        account.setBalance(5000.0);
        return account;
    }
}
