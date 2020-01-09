package com.maggio.telegram.togglbot.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public class User {

    private int id;

    @JsonAlias("isBot")
    private boolean bot;

    private String firstName;

    private String lastName;

    private String languageCode;

}
