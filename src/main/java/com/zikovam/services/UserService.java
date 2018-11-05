package com.zikovam.services;

import com.zikovam.dao.UserDao;
import com.zikovam.dao.UserDaoImpl;
import com.zikovam.entity.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path ("users")
public class UserService {

    private static final String USER_404 = "User with such id doesn\'t exist";
    private static final String SUCCESS = "Success";
    private static final String USER_ALREADY_EXIST = "User already exist";

    private UserDao usersDao = new UserDaoImpl();

    public UserService () {
    }

    @GET
    @Path ("{id}")
    @Produces (MediaType.APPLICATION_JSON)
    public Response findUser (@PathParam ("id") String id) {
        if (longChecking(id)) {
            User user = usersDao.gerUserById(Long.parseLong(id));

            if (null != user) {
                return Response
                        .status(Response.Status.OK)
                        .entity(user.toString())
                        .build();
            }
        }
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(USER_404)
                .build();
    }


    @POST
    @Path("{userName}")
    public Response createUser (@PathParam ("userName") String userName) {

        if (null == usersDao.getUserByUsername(userName)) {
            User user = new User(userName);
            usersDao.createUser(user);

            return Response
                    .status(Response.Status.CREATED)
                    .entity(SUCCESS)
                    .build();
        }

        return Response
                .status(Response.Status.FORBIDDEN)
                .entity(USER_ALREADY_EXIST)
                .build();
    }

    @PUT
    @Path ("{oldUsername}/{newUsername}")
    public Response updateUser (@PathParam ("oldUsername") String oldUsername,
                                @PathParam ("newUsername") String newUsername) {

        User user = usersDao.getUserByUsername(oldUsername);

        if (null != user) {
            user.setName(newUsername);
            usersDao.updateUser(user);

            return Response
                    .status(Response.Status.OK)
                    .entity(SUCCESS)
                    .build();
        }

        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(USER_404)
                .build();
    }

    @DELETE
    @Path ("{id}")
    public Response deleteUser (@PathParam ("id") String id) {
        if (longChecking(id)) {
            User user = usersDao.gerUserById(Long.parseLong(id));
            if (null != user) {
                usersDao.deleteUser(user);
                return Response
                        .status(Response.Status.OK)
                        .entity(SUCCESS)
                        .build();
            }
        }
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(USER_404)
                .build();
    }

    public void updateUser (User user) {
        usersDao.updateUser(user);
    }

    @GET
    @Produces (MediaType.APPLICATION_JSON)
    public Response findAllUsers () {
        List<String> users = new ArrayList<>();
        //transform output to avoid recursion
        usersDao.getAllUsers().forEach(u -> users.add(u.toString()));
        return Response
                .status(Response.Status.OK)
                .entity(users)
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
