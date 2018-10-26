package com.zikovam.dao;

import com.zikovam.entity.Account;

public interface AccountDao {

    Account getAccountById(long id);

    void createAccount(Account account);
    void deleteAccount(Account account);
    void updateAccount(Account account);
}
