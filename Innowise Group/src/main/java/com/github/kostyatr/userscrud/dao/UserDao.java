package com.github.kostyatr.userscrud.dao;

import com.github.kostyatr.userscrud.model.User;

import java.io.IOException;
import java.util.List;

public interface UserDao {
    List<User> getUsers() throws IOException;

    boolean saveUsers(List<User> users);

    boolean saveUsers(List<User> users, String backupFileName);
}
