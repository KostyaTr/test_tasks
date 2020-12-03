package com.github.kostyatr.userscrud.service;

import com.github.kostyatr.userscrud.model.User;

import java.util.List;

public interface UserService {
    boolean mailCheck(String mail);

    boolean phoneNumberCheck(String phoneNumber);

    List<User> getUsers();

    List<User> getUserByFullName(String fullName);

    void updateUserInfo(User newUser, User oldUser);

    boolean addUser(User user);

    boolean deleteUser(User user);

    boolean saveUsers();

    boolean saveUsers(String backupFileName);
}
