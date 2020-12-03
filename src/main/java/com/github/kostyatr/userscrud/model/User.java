package com.github.kostyatr.userscrud.model;

import java.util.List;
import java.util.Objects;

public class User {
    private String firstName;
    private String lastName;
    private String mail;
    private List<String> roles;
    private List<String> phoneNumbers;

    public User(String firstName, String lastName, String mail, List<String> roles, List<String> phoneNumbers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.roles = roles;
        this.phoneNumbers = phoneNumbers;
    }

    public User(){}

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mail='" + mail + '\'' +
                ", roles=" + roles +
                ", phoneNumbers=" + phoneNumbers +
                '}';
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getFirstName(), user.getFirstName()) &&
                Objects.equals(getLastName(), user.getLastName()) &&
                Objects.equals(getMail(), user.getMail()) &&
                Objects.equals(getRoles(), user.getRoles()) &&
                Objects.equals(getPhoneNumbers(), user.getPhoneNumbers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getMail(), getRoles(), getPhoneNumbers());
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
}
