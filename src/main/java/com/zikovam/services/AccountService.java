package com.zikovam.services;

import com.zikovam.dao.AccountDao;
import com.zikovam.dao.AccountDaoImpl;
import com.zikovam.entity.Account;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path ("accounts")
public class AccountService {

    private static final String ACCOUNT_404 = "User with such id doesn\'t exist";
    private static final String SUCCESS = "Success";
    private static final String ERROR = "Error";

    private AccountDao accountDao = new AccountDaoImpl();

    public AccountService () {
    }

    @GET
    @Path ("{id}")
    public Response findAccount (@PathParam ("id") String id) {
        if (longChecking(id)) {
            Account account = accountDao.getAccountById(Long.parseLong(id));
            if (null != account) {
                return Response
                        .status(Response.Status.OK)
                        .entity(account.toString())
                        .build();
            }
        }
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(ACCOUNT_404)
                .build();
    }

    @POST
    @Path ("{id}/{newSum}")
    public Response updateAccount (@PathParam ("id") String id,
                                   @PathParam ("newSum") String newSum) {
        if (longChecking(id) && longChecking(newSum)) {
            Account account = accountDao.getAccountById(Long.parseLong(id));
            if (null != account) {
                account.setBalance(Long.parseLong(newSum));
                return Response
                        .status(Response.Status.OK)
                        .entity(account.toString())
                        .build();
            }
        }

        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(ERROR)
                .build();
    }

    @DELETE
    @Path ("{id}")
    public Response deleteUser (@PathParam ("id") String id) {
        if (longChecking(id)) {
            Account account = accountDao.getAccountById(Long.parseLong(id));
            if (null != account) {
                accountDao.deleteAccount(account);
                return Response
                        .status(Response.Status.OK)
                        .entity(SUCCESS)
                        .build();
            }
        }
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(ACCOUNT_404)
                .build();
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
