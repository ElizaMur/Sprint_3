package ru.yandex.practicum.scooter.api.model;

import org.apache.commons.lang3.RandomStringUtils;

public class CourierWithRequiredFields {
    private String login;
    private String password;

    public CourierWithRequiredFields(String login, String password) {
        this.login = login;
        this.password = password;

    }

    public static CourierWithRequiredFields getRandomCourierRequiredFields() {
        String login = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        return new CourierWithRequiredFields(login, password);

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

}
