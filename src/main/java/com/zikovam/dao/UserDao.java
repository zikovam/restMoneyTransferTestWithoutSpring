package com.zikovam.dao;

import com.zikovam.entity.User;

import java.util.List;

public interface UserDao {

    List<User> getAllUsers();
    User gerUserById(long id);
    User getUserByUsername (String username);

    void createUser(User user);
    void updateUser(User user);
    void deleteUser(User user);

}
