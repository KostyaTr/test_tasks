package com.github.kostyatr.userscrud.service.impl;

import com.github.kostyatr.userscrud.dao.UserDao;
import com.github.kostyatr.userscrud.model.User;
import com.github.kostyatr.userscrud.service.UserService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultUserService implements UserService {

    private UserDao userDao;
    private List<User> users;

    public DefaultUserService(UserDao userDao) throws IOException {
        this.userDao = userDao;
        this.users = userDao.getUsers();
    }

    @Override
    public boolean mailCheck(String mail) {
        return mail.matches("^.+@.+\\..+$");
    }

    @Override
    public boolean phoneNumberCheck(String phoneNumber){
        return phoneNumber.matches("375[0-9]{2}\\s[0-9]{7}");
    }

    @Override
    public List<User> getUserByFullName(String fullName) {
        return this.users.stream()
                .filter(u -> (u.getFirstName() + " " + u.getLastName()).equals(fullName))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getUsers() {
        return this.users;
    }

    @Override
    public void updateUserInfo(User newUser, User oldUser) {
        this.users.set(this.users.indexOf(oldUser), newUser);
    }

    @Override
    public boolean addUser(User user) {
        return this.users.add(user);
    }

    @Override
    public boolean deleteUser(User user) {
        return this.users.remove(user);
    }

    @Override
    public boolean saveUsers() {
        return userDao.saveUsers(this.users);
    }

    // used if it is impossible to save to the default file
    @Override
    public boolean saveUsers(String backupFileName) {
        return userDao.saveUsers(this.users, backupFileName);
    }
}
