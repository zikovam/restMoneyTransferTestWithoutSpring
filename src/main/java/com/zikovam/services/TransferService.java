package com.zikovam.services;

import com.zikovam.dao.AccountDao;
import com.zikovam.dao.AccountDaoImpl;
import com.zikovam.dao.UserDao;
import com.zikovam.dao.UserDaoImpl;
import com.zikovam.entity.Account;
import com.zikovam.entity.User;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;

@Path ("transfer")
public class TransferService {
    private UserDao userDao = new UserDaoImpl();
    AccountDao accountDao = new AccountDaoImpl();

    private static final String ERROR = "Error";

    public TransferService () {
    }

    @POST
    @Path ("{userFrom}/{accountIdFrom}/{UserTo}/{accountIdTo}/{sum}")
    @Produces (MediaType.APPLICATION_JSON)
    public Response transferMoney (@PathParam ("userFrom") String userFrom,
                                   @PathParam ("accountIdFrom") String accountIdFrom,
                                   @PathParam ("UserTo") String userTo,
                                   @PathParam ("accountIdTo") String accountIdTo,
                                   @PathParam ("sum") String sum) {

        if (longChecking(sum) && longChecking(accountIdFrom) && longChecking(accountIdTo)) {
            Account accountFromEntity = getAccountFromUserByIdOrByUsername(userFrom, accountIdFrom);
            Account accountToEntity = getAccountFromUserByIdOrByUsername(userTo, accountIdTo);
            if (null != accountFromEntity && null != accountToEntity) {

                Long moneyTransferAmount = Long.parseLong(sum);

                if (accountFromEntity.getBalance() > moneyTransferAmount) {
                    Long currentSum = accountFromEntity.getBalance();
                    accountFromEntity.setBalance(currentSum - moneyTransferAmount);

                    currentSum = accountToEntity.getBalance();
                    accountToEntity.setBalance(currentSum + moneyTransferAmount);

                    accountDao.updateAccount(accountFromEntity);
                    accountDao.updateAccount(accountToEntity);

                    return Response.status(Response.Status.OK)
                            .entity(Arrays.asList(accountFromEntity.toString(),accountToEntity.toString()))
                            .build();
                }

            }
        }

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(ERROR)
                .build();
    }

    private Account getAccountFromUserByIdOrByUsername (String userInfo, String userAccountId) {
        User user;
        if (longChecking(userInfo)) {
            user = userDao.gerUserById(Long.parseLong(userInfo));
        } else {
            user = userDao.getUserByUsername(userInfo);
        }

        Account account = null;
        if (null != user) {
            account = user.getAccounts().stream()
                    .filter(a -> a.getId().equals(Long.parseLong(userAccountId)))
                    .findFirst()
                    .orElse(null);
        }

        return account;
    }

    private boolean longChecking (String id) {
        try {
            Long.parseLong(id);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }
}
