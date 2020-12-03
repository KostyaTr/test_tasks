package com.github.kostyatr.userscrud;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.kostyatr.userscrud.model.User;
import com.github.kostyatr.userscrud.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class UsersCrudApplication implements CommandLineRunner {

    private UserService userService;
    private final ObjectMapper jsonMapper;

    public UsersCrudApplication(UserService userService) {
        jsonMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        this.userService = userService;
    }

    public static void main(String[] args) {
        SpringApplication.run(UsersCrudApplication.class, args);
    }

    @Override
    public void run(String... args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("1. Get all users");
            System.out.println("2. Get user");
            System.out.println("3. Update user info");
            System.out.println("4. Add user");
            System.out.println("5. Delete user");
            System.out.println("6. Exit");
            System.out.print("Please select an item from 1 to 6: ");
            switch (reader.readLine()) {
                case "1":
                    System.out.println(jsonMapper.writeValueAsString(userService.getUsers()));
                    break;
                case "2":
                    System.out.print("Please enter user's full name (first name + last name): ");
                    List<User> userByFullName = userService.getUserByFullName(reader.readLine());
                    if (userByFullName.size() == 0) {
                        System.out.println("No user with this name was found");
                    } else {
                        System.out.println(jsonMapper.writeValueAsString(userByFullName));
                    }
                    break;
                case "3":
                    if (updateUserInfo(reader)){
                        System.out.println("User information has been updated");
                    } else {
                        System.out.println("Update has been canceled");
                    }
                    break;
                case "4":
                    if (userService.addUser(createUser(reader))){
                        System.out.println("User has been added");
                    } else {
                        System.out.println("An unknown error has occurred. User has not been added");
                    }
                    break;
                case "5":
                    if (deleteUser(reader)) {
                        System.out.println("User has been deleted");
                    } else {
                        System.out.println("User has not been deleted");
                    }
                    break;
                case "6":
                    if (!userService.saveUsers()) {
                        System.out.println("Impossible to save to the default file");
                        System.out.print("Please enter location to another file: ");
                        String backupFileName = reader.readLine();
                        while (!userService.saveUsers(backupFileName)) {
                            System.out.println("Impossible to save to the file: " + backupFileName);
                            System.out.print("Proceed without saving to file? (Yes/(anything else))");
                            if (reader.readLine().equals("Yes")){
                                break;
                            }
                            System.out.print("Please enter location to another file: ");
                            backupFileName = reader.readLine();
                        }
                    }
                    System.exit(0);
            }
        }
    }

    private boolean updateUserInfo(BufferedReader reader) throws IOException {
        System.out.print("Please enter the full name of the user you want to update (first name + last name): ");
        List<User> matchingUsers = userService.getUserByFullName(reader.readLine());
        if (matchingUsers.size() == 0) {
            System.out.println("No user was found with this name was found");
            return false;
        } else {
            if (matchingUsers.size() == 1) {
                return update(reader, matchingUsers.get(0));
            } else {
                System.out.println("Several users with this name were found");
                System.out.println(jsonMapper.writeValueAsString(matchingUsers));
                while (true){
                    System.out.print("Please select the user you want to update (1,2,3..): ");
                    int answer;
                    try {
                        answer = Integer.parseInt(reader.readLine());
                    } catch (NumberFormatException e){
                        System.out.println("Please enter a number");
                        continue;
                    }
                    if (answer >= 1 && answer <= matchingUsers.size()) {
                        return update(reader, matchingUsers.get(answer - 1));
                    } else {
                        System.out.println("Please enter a valid number");
                    }
                }
            }
        }
    }

    private boolean update(BufferedReader reader, User user) throws IOException {
        User newUser = user;
        while (true) {
            System.out.println(jsonMapper.writeValueAsString(newUser));
            System.out.println("1. Change user's first name");
            System.out.println("2. Change user's last name");
            System.out.println("3. Change user's mail");
            System.out.println("4. Change user's roles");
            System.out.println("5. Change user's phone numbers");
            System.out.println("6. Finish update");
            System.out.println("7. Cancel update");
            System.out.print("Please select an item from 1 to 7: ");
            switch (reader.readLine()) {
                case "1":
                    System.out.print("Enter user's new first name: ");
                    newUser.setFirstName(reader.readLine());
                    break;
                case "2":
                    System.out.print("Enter user's new last name: ");
                    newUser.setLastName(reader.readLine());
                    break;
                case "3":
                    System.out.print("Enter user's new mail (Example: any@email.com): ");
                    String mail = reader.readLine();
                    if (userService.mailCheck(mail)){
                        newUser.setMail(mail);
                    } else {
                        System.out.println("Incorrect Input.");
                    }
                    break;
                case "4":
                    System.out.print("Enter user's role: ");
                    user.setRoles(getRoles(reader));
                    break;
                case "5":
                    System.out.print("Enter user's new phone number: ");
                    user.setPhoneNumbers(getPhoneNumbers(reader));
                    break;
                case "6":
                    userService.updateUserInfo(newUser, user);
                    return true;
                case "7":
                    return false;
            }
        }
    }

    private boolean deleteUser(BufferedReader reader) throws IOException {
        System.out.print("Please enter the full name of the user you want to delete (first name + last name): ");
        List<User> matchingUsers = userService.getUserByFullName(reader.readLine());
        if (matchingUsers.size() == 0) {
            System.out.println("No user was found with this name was found");
            return false;
        } else {
            if (matchingUsers.size() == 1) {
                return confirmDelete(reader, matchingUsers.get(0));
            } else {
                System.out.println("Several users with this name were found");
                System.out.println(jsonMapper.writeValueAsString(matchingUsers));
                while (true){
                    System.out.print("Please select the user you want to delete (1,2,3..): ");
                    int answer;
                    try {
                        answer = Integer.parseInt(reader.readLine());
                    } catch (NumberFormatException e){
                        System.out.println("Please enter a number");
                        continue;
                    }
                    if (answer >= 1 && answer <= matchingUsers.size()) {
                        return confirmDelete(reader, matchingUsers.get(answer - 1));
                    } else {
                        System.out.println("Please enter a valid number");
                    }
                }
            }
        }
    }

    private boolean confirmDelete(BufferedReader reader, User user) throws IOException {
        System.out.println(jsonMapper.writeValueAsString(user));
        while (true){
            System.out.print("You want to delete this user? (y/n): ");
            switch (reader.readLine()) {
                case "y":
                    return userService.deleteUser(user);
                case "n":
                    return false;
                default:
                    System.out.println("Incorrect input");
            }
        }
    }

    private User createUser(BufferedReader reader) throws IOException {
        User user = new User();
        System.out.print("Enter user's first name: ");
        user.setFirstName(reader.readLine());
        System.out.print("Enter user's last name: ");
        user.setLastName(reader.readLine());

        System.out.print("Enter user's mail: ");
        String mail;
        while (true) {
            mail = reader.readLine();
            if (userService.mailCheck(mail)) {
                break;
            }
            System.out.print("Incorrect input. Example: any@email.com. Please enter again: ");
        }
        user.setMail(mail);

        System.out.print("Enter user's role: ");
        user.setRoles(getRoles(reader));

        System.out.print("Enter user's phone number: ");
        user.setPhoneNumbers(getPhoneNumbers(reader));

        return user;
    }

    private List<String> getRoles(BufferedReader reader) throws IOException {
        List<String> roles = new ArrayList<>(3);
        roles.add(reader.readLine());
        for (int i = 0; i < 2; i++) {
            System.out.print("Do you want to add another role? (y/n) ");
            switch (reader.readLine()) {
                case "y":
                    System.out.print("Enter another user's role: ");
                    roles.add(reader.readLine());
                    break;
                case "n":
                    i = 1; // to exit loop
                    break;
                default:
                    System.out.println("Incorrect input");
                    i--;
            }
        }
        return roles;
    }

    private List<String> getPhoneNumbers(BufferedReader reader) throws IOException {
        List<String> phoneNumbers = new ArrayList<>(3);
        phoneNumbers.add(getPhoneNumber(reader));
        for (int i = 0; i < 2; i++) {
            System.out.print("Do you want to add another phone number? (y/n) ");
            switch (reader.readLine()) {
                case "y":
                    System.out.print("Enter another user's phone number: ");
                    phoneNumbers.add(getPhoneNumber(reader));
                    break;
                case "n":
                    i = 2; // to exit loop
                    break;
                default:
                    System.out.println("Incorrect input");
                    i--;
            }
        }
        return phoneNumbers;
    }

    private String getPhoneNumber(BufferedReader reader) throws IOException {
        String phoneNumber;
        while (true) {
            phoneNumber = reader.readLine();
            if (userService.phoneNumberCheck(phoneNumber)) {
                break;
            }
            System.out.print("Incorrect input. Example: 37500 1234567. Please enter again: ");
        }
        return phoneNumber;
    }
}