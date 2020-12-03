package com.github.kostyatr.userscrud.dao.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kostyatr.userscrud.dao.UserDao;
import com.github.kostyatr.userscrud.model.User;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
@PropertySource("classpath:application.properties")
public class DefaultUserDao implements UserDao {

    private final static Logger logger = Logger.getLogger(DefaultUserDao.class.getName());
    private String fileName;
    private final ObjectMapper jsonMapper;

    public DefaultUserDao(Environment env) {
        fileName = env.getProperty("json.file.location");
        jsonMapper = new ObjectMapper();
    }

    @Override
    public List<User> getUsers() throws IOException {
        List<User> users;
        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            users = new ArrayList<>(Arrays.asList(jsonMapper.readValue(fileInputStream, User[].class)));
        } catch (IOException e) {
            throw new IOException(e);
        }
        return users;
    }

    @Override
    public boolean saveUsers(List<User> users) {
        return save(users, this.fileName);
    }

    // used if it is impossible to save to the default file
    @Override
    public boolean saveUsers(List<User> users, String backupFileName) {
        return save(users, backupFileName);
    }

    private boolean save(List<User> users, String fileName){
        try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
            jsonMapper.writeValue(outputStream, users);
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, "Unable to save array to file", e);
            logger.log(Level.INFO, "array: " + users);
            logger.log(Level.INFO, "file location: " + fileName);
            return false;
        }
        return true;
    }
}