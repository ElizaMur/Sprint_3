package ru.yandex.practicum.scooter.api.model;

import org.apache.commons.lang3.RandomStringUtils;

public class CourierCredentials {
    public String login;
    public String password;

    public CourierCredentials(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public static CourierCredentials getRandomCourierCredentials(){
        String login = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        return new CourierCredentials(login, password);
    }

    public CourierCredentials() {
    }
}
