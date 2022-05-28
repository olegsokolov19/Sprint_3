package ru.yandex.practicum.scooter.api.model;

import org.apache.commons.lang3.RandomStringUtils;

public class CourierModel {

    private String login;
    private String password;
    private String firstName;

    public CourierModel() {
    }

    public CourierModel(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public CourierModel(String login, String password, String firstName) {
        this(login, password);
        this.firstName = firstName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

}
